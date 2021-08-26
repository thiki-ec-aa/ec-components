# ec-components


## how to download the packages

[docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)

add content below into ~/.m2/settings.xml:

```xml

<settings>

  ...
  
  <repositories>
   <repository>
    <id>github</id>
    <!-- <url>https://maven.pkg.github.com/joeaniu/*</url> -->
    <url>https://maven.pkg.github.com/thiki-ec-aa/*</url>
    <snapshots>
      <enabled>true</enabled>
      <!-- can be "always", "daily" (default), "interval:XXX" (in minutes) or "never" (only if it doesn't exist locally).-->
      <updatePolicy>interval:30</updatePolicy>
    </snapshots>
   </repository>

  </repositories>             
  
  <servers>
    <server>
      <id>github</id>
      <username>USERNAME</username>
      <!-- PAT for download the package from thiki-ec-aa -->
      <password>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</password>
    </server>
  </servers>
</settings>

```

