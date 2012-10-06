JavaMysqlShhConnectioninator
============================

Connect to a MySQL server over ssh with Java, using JsCH

Author: Dave Eargle
Contact: dave@daveeargle.com

Tacky java code for connecting to a mysql server over an ssh connection. Putting
it on github to share with some teammates

Be sure to add the MySQL Connector/J driver to your project - Inside the projects
tab, right click on the Libraries node and select the "Add Library" option. From
the list of options, select MySQL JDBC Driver.

Be sure to set your dbconnect info in DBConnect.java and also your ssh connection
info in PortForwarder before using the code

Also, you'll need the JSch jar from http://www.jcraft.com/jsch/. Scroll down
on that page to find the .jar download link. Then, if you're in NetBeans, 
right click "libraries" in the project list, then click add, then navigate to 
the JAR you just downloaded.