FROM gitpod/workspace-full-vnc

SHELL ["/bin/bash", "-c"]
RUN sudo apt update && sudo apt upgrade -y && sudo apt autoremove #&& sudo apt install qt5-default -y
RUN source "/home/gitpod/.sdkman/bin/sdkman-init.sh"  \
    && sdk install java 17.0.7-zulu < /dev/null
