# NoderefQRCode

Adds an "Add by QR code" entry to the "Friends" menu which renders the node's darknet reference as a QR code
and decodes pictures of QR codes as text. This is in the hope that it's easier to trade images or pieces of paper
than blocks of precisely formatted text.

### Building

* [Fred 0.7.5.1436](http://downloads.freenetproject.org/alpha/freenet-build01436.jar)
* [Freenet-ext 29](http://downloads.freenetproject.org/alpha/freenet-ext.jar)

Add to Maven with:

    mvn install:install-file -Dfile=freenet.jar -DgroupId=org.freenetproject -DartifactId=fred -Dversion=0.7.5.1436 -Dpackaging=jar
    mvn install:install-file -Dfile=freenet-ext.jar -DgroupId=org.freenetproject -DartifactId=freenet-ext -Dversion=29 -Dpackaging=jar

Other versions of Fred should probably work too; feel free to change the pom.

That done, `mvn package` will fetch remaining dependencies from the central Maven repository and build the package.
The jar of interest is `target/NoderefQRCode-0.0.1-jar-with-dependencies.jar`.
