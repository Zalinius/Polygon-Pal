
void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: env.GIT_URL],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}


pipeline {
	agent any
	stages {
		// Note that the agent automatically checks out the source code from Github	

    	stage('Compile') { 
            steps {
            	sh 'mvn --batch-mode compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn --batch-mode test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package Jar') {
            steps {
                sh 'mvn --batch-mode -DskipTests clean package'
            }
        }
        stage('Deploy') {
        	when {
 				branch 'main'
	       	}
			environment {
				GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
				PROJECT_NAME = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 
			}
			steps {
				sh 'sudo butler push target/${PROJECT_NAME}-${GAME_VERSION}.jar zalinius/polygon-pal:win-linux-mac -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'
	       	}
	    }
	}
	post {
    	success {
       		setBuildStatus('Build succeeded', 'SUCCESS');
    	}
    	failure {
       		setBuildStatus('Build failed', 'FAILURE');
    	}		
	}
}