
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
        	//when {
 			//	branch 'main'
	       	//}
			environment {
				GAME_VERSION = sh script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true 
				PROJECT_NAME = sh script: 'mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout', returnStdout: true 
				
				JAVA_8_HOME = '/usr/lib/jvm/java-8-openjdk-amd64/bin/'
				LAUNCH4J_HOME = '/usr/local/bin/launch4j/'
				JRE_WIN = '/usr/local/bin/OpenJDK11U-jre_x64_windows_hotspot_11.0.10_9.zip'
			}
			steps {
				//Make EXE
				sh '${JAVA_8_HOME}/java -jar ${LAUNCH4J_HOME}/launch4j.jar windows_exe_config.xml'
				//Get JRE
				unzip zipFile: '$JRE_WIN', dir: 'target/windows/jre/'
				
			//	sh 'sudo butler push target/windows/ zalinius/polygon-pal:win- -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'				
			//	sh 'sudo butler push target/${PROJECT_NAME}-${GAME_VERSION}.jar zalinius/polygon-pal:win-linux-mac -i /home/zalinius/.config/itch/butler_creds --userversion $GAME_VERSION --fix-permissions --if-changed'
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