# Openshift binary deploy pipeline
Openshift CI/CD pipeline implementation based on [Leandro Berettas's demo](https://github.com/leandroberetta/openshift-cicd-pipelines) using a binary file for deploy.
For this demo, i'm using [this](https://github.com/sonatype-nexus-community/deployment-reference-architecture/blob/master/OpenShift/nexus-repository-manager.yaml) nexus template file.

## Introduction
This is a demo repository with the basic structure and instructions to deploy a Jenkins Pipeline provisioning a war file.

## Setup
You can either run the `setup.sh` or copy/paste each command on your own (it may help the user undertand what's going on here).

```
oc new-project genexus-dev
oc new-project genexus-test
oc new-project genexus-prod
oc new-project jenkins

oc new-app sonatype/nexus -n jenkins

echo "apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nexus
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi" | oc create -f - -n jenkins

oc set volume dc/nexus --add --name=nexus-volume-1 -t pvc --claim-name=nexus --mount-path=/sonatype-work --overwrite

oc expose svc nexus -n jenkins

oc create secret generic repository-credentials --from-file=ssh-privatekey=$HOME/.ssh/id_rsa --type=kubernetes.io/ssh-auth -n genexus-dev
oc label secret repository-credentials credential.sync.jenkins.openshift.io=true -n genexus-dev
oc annotate secret repository-credentials 'build.openshift.io/source-secret-match-uri-1=ssh://github.com/*' -n genexus-dev

oc create secret generic repository-credentials --from-file=ssh-privatekey=$HOME/.ssh/id_rsa --type=kubernetes.io/ssh-auth -n jenkins

oc label secret repository-credentials credential.sync.jenkins.openshift.io=true -n jenkins

oc new-build jenkins:2 --binary --name custom-jenkins -n jenkins
oc start-build custom-jenkins --from-dir=./jenkins --wait -n jenkins
oc new-app --template=jenkins-ephemeral --name=jenkins -p JENKINS_IMAGE_STREAM_TAG=custom-jenkins:latest -p NAMESPACE=jenkins -n jenkins

oc adm policy add-role-to-user edit system:serviceaccount:jenkins:jenkins -n genexus-dev
oc adm policy add-role-to-user edit system:serviceaccount:jenkins:jenkins -n genexus-test
oc adm policy add-role-to-user edit system:serviceaccount:jenkins:jenkins -n genexus-prod

oc create -f ./templates/ci-pipeline.yaml -n genexus-dev
oc create -f ./templates/cd-pipeline.yaml -n genexus-dev

oc new-app --template ci-pipeline -p APP_NAME=genexus -p GIT_REPO=ssh://git@github.com/juanchiriera/openshift-binary-deploy-pipeline.git -p GIT_BRANCH=master -n genexus-dev

oc new-app --template cd-pipeline -p APP_NAME=genexus -p GIT_REPO=ssh://git@github.com/juanchiriera/openshift-binary-deploy-pipeline.git -p GIT_BRANCH=master -n genexus-dev

```

>To get the base project top build .war file, use [this repo](https://github.com/jboss-openshift/openshift-quickstarts/tree/master/tomcat-jdbc).