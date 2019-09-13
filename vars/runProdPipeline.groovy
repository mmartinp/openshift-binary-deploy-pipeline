#!/usr/bin/env groovy

def call(parameters) {
    pipeline {
        agent {
            label parameters.agent
        }
        options {
            skipDefaultCheckout()
            disableConcurrentBuilds()
        }
        stages {
            stage("Checkout") {
                steps {      
                    gatherParameters(parameters)
                    gitClone()
                }
            }
            stage("Compile") {
                steps {
                    sh parameters.compileCommands
                }
            }
            stage("Build Image") {
                steps {
                    applyTemplate(project: env.PROD_PROJECT, 
                                  application: env.APP_NAME, 
                                  template: env.APP_TEMPLATE, 
                                  parameters: env.APP_TEMPLATE_PARAMETERS_PROD,
                                  replaceConfig: env.APP_REPLACE_CONFIG_PROD,
                                  deploymentPatch: env.APP_DEPLOYMENT_PATCH_PROD,
                                  createBuildObjects: true)

                    buildImage(project: env.PROD_PROJECT, 
                               application: env.APP_NAME, 
                               artifactsDir: parameters.artifactsDir,
                               artifactFile: parameters.artifactFile)
                }
            }
            stage("Deploy Prod") {
                steps {
                    script {
                        env.TAG_NAME = getVersion(parameters.agent)
                    }   
                    
                    tagImage(srcProject: env.PROD_PROJECT, 
                             srcImage: env.IMAGE_NAME, 
                             srcTag: "latest", 
                             dstProject: env.PROD_PROJECT, 
                             dstImage: env.IMAGE_NAME,
                             dstTag: env.TAG_NAME)
                    
                    deployImage(project: env.PROD_PROJECT, 
                                application: env.APP_NAME, 
                                image: env.IMAGE_NAME, 
                                tag: env.TAG_NAME)
                }
            }
        }
    }    
}