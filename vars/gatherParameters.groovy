#!/usr/bin/env groovy

def call(parameters) {
    env.APP_NAME = parameters.application
    env.IMAGE_NAME = parameters.application
    env.TAG_NAME = parameters.version

    env.PROJECT = (parameters.application) ? parameters.application : env.JOB_NAME.split("/")[0].split("-")[0]

    env.DEV_PROJECT = (parameters.devProject) ? parameters.devProject: "${env.PROJECT}-dev"
    env.TEST_PROJECT = (parameters.qaProject) ? parameters.qaProject: "${env.PROJECT}-qa"
    env.UAT_PROJECT = (parameters.uatProject) ? parameters.uatProject: "${env.PROJECT}-uat"
    env.PROD_PROJECT = (parameters.prodProject) ? parameters.prodProject: "${env.PROJECT}-prod"

    env.APP_OPENSHIFT_DIR = (env.OPENSHIFT_DIR) ? env.OPENSHIFT_DIR : "./openshift"
    env.APP_TEMPLATE = (parameters.applicationTemplate) ? parameters.applicationTemplate : "./${env.APP_OPENSHIFT_DIR}/template.yaml"
    env.APP_TEMPLATE_PARAMETERS_DEV = (parameters.applicationTemplateParametersDev) ? parameters.applicationTemplateParametersDev : "./${env.APP_OPENSHIFT_DIR}/environments/dev/templateParameters.txt"
    env.APP_TEMPLATE_PARAMETERS_TEST = (parameters.applicationTemplateParametersTest) ? parameters.applicationTemplateParametersTest :  "./${env.APP_OPENSHIFT_DIR}/environments/qa/templateParameters.txt"
    env.APP_TEMPLATE_PARAMETERS_UAT = (parameters.applicationTemplateParametersUat) ? parameters.applicationTemplateParametersUat :  "./${env.APP_OPENSHIFT_DIR}/environments/uat/templateParameters.txt"
    env.APP_TEMPLATE_PARAMETERS_PROD = (parameters.applicationTemplateParametersProd) ? parameters.applicationTemplateParametersProd :  "./${env.APP_OPENSHIFT_DIR}/environments/prod/templateParameters.txt"
    env.APP_DEPLOYMENT_PATCH_DEV = (parameters.applicationDeploymentPatchDev) ? parameters.applicationDeploymentPatchDev : "./${env.APP_OPENSHIFT_DIR}/environments/dev/deploymentPatch.yaml"
    env.APP_DEPLOYMENT_PATCH_TEST = (parameters.applicationDeploymentPatchTest) ? parameters.applicationDeploymentPatchTest : "./${env.APP_OPENSHIFT_DIR}/environments/qa/deploymentPatch.yaml"
    env.APP_DEPLOYMENT_PATCH_UAT = (parameters.applicationDeploymentPatchUat) ? parameters.applicationDeploymentPatchUat : "./${env.APP_OPENSHIFT_DIR}/environments/uat/deploymentPatch.yaml"
    env.APP_DEPLOYMENT_PATCH_PROD = (parameters.applicationDeploymentPatchProd) ? parameters.applicationDeploymentPatchProd : "./${env.APP_OPENSHIFT_DIR}/environments/prod/deploymentPatch.yaml"
    env.APP_REPLACE_CONFIG_DEV = (parameters.applicationReplaceConfigDev) ? parameters.applicationReplaceConfigDev : "./${env.APP_OPENSHIFT_DIR}/environments/dev/replaceConfig.yaml"
    env.APP_REPLACE_CONFIG_TEST = (parameters.applicationReplaceConfigTest) ? parameters.applicationReplaceConfigTest : "./${env.APP_OPENSHIFT_DIR}/environments/qa/replaceConfig.yaml"
    env.APP_REPLACE_CONFIG_UAT = (parameters.applicationReplaceConfigUat) ? parameters.applicationReplaceConfigUat : "./${env.APP_OPENSHIFT_DIR}/environments/uat/replaceConfig.yaml"
    env.APP_REPLACE_CONFIG_PROD = (parameters.applicationReplaceConfigProd) ? parameters.applicationReplaceConfigProd : "./${env.APP_OPENSHIFT_DIR}/environments/prod/replaceConfig.yaml"
    env.APP_INT_TEST_AGENT = (parameters.applicationIntegrationTestAgent) ? parameters.applicationIntegrationTestAgent : "./${env.APP_OPENSHIFT_DIR}/environments/qa/integration-test/int-test.yaml"
    env.APP_INT_UAT_AGENT = (parameters.applicationIntegrationUatAgent) ? parameters.applicationIntegrationUatAgent : "./${env.APP_OPENSHIFT_DIR}/environments/uat/integration-test/int-test.yaml"
    env.APP_INT_TEST_COMMANDS = (parameters.applicationIntegrationTestCommands) ? parameters.applicationIntegrationTestCommands : "./${env.APP_OPENSHIFT_DIR}/environments/qa/integration-test/int-test.groovy"
    env.APP_INT_UAT_COMMANDS = (parameters.applicationIntegrationUatCommands) ? parameters.applicationIntegrationUatCommands : "./${env.APP_OPENSHIFT_DIR}/environments/uat/integration-test/int-test.groovy"
}