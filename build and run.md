Install JDK 8 and set it as the project SDK

Intellij plugin development sdk: Installation folder (before Contents)

run JRE: /Applications/Android Studio.app/Contents
run flags: -Xmx512m -Xms256m -XX:MaxPermSize=250m -ea -Dapple.laf.useScreenMenuBar=false

mvp-...-plugin.iml file content
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module type="PLUGIN_MODULE" version="4">
  <component name="DevKit.ModuleBuildProperties" url="file://$MODULE_DIR$/resources/META-INF/plugin.xml" />
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/resources" type="java-resource" />
      <sourceFolder url="file://$MODULE_DIR$/res" type="java-resource" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
    <orderEntry type="library" name="KotlinJavaRuntime" level="project" />
    <orderEntry type="library" name="lib" level="project" />
  </component>
</module>
```