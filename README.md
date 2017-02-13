### Siggi CI Bricks

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8db9618f5a9443c7a6b43f3b6f5ed41f)](https://www.codacy.com/app/jbellmann/siggi-ci-bricks?utm_source=github.com&utm_medium=referral&utm_content=siggi-ci/siggi-ci-bricks&utm_campaign=badger)

is a collection of modules needed to assemble the Siggi CI app.

#### Build

Maven-Wrapper is used to build the whole project:

```
./mvnw clean install
```

#### Skip tests during 'release:perform'

add '${arguments}' placeholder in release-plugin configuration

```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-release-plugin</artifactId>
                <version>2.5.3</version>
                <configuration>
                    <tagNameFormat>@{artifactId}-@{project.version}</tagNameFormat>
                    <mavenExecutorId>forked-path</mavenExecutorId>
                    <autoVersionSubmodules>true</autoVersionSubmodules>
                    <useReleaseProfile>false</useReleaseProfile>
                    <arguments>-Pcustom-release,maven ${arguments}</arguments>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

then put as many arguments as needed

```
mvn release:perform -Darguments="-Dmaven.test.skip" --batch-mode
```