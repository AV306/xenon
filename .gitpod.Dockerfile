FROM gitpod/workspace-full-vnc

SHELL ["/bin/bash", "-c"]
RUN sudo apt install libqt5gui5 -y && sudo apt install libqt5xml5 -y
RUN source "/home/gitpod/.sdkman/bin/sdkman-init.sh"  \
    && sdk install java 17.0.7-zulu < /dev/null
