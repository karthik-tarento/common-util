# Publishing to Maven Central

This guide provides step-by-step instructions for publishing the `common-util` library to Maven Central.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- GPG (GnuPG) installed on your system

## One-Time Setup

### Step 1: Install GPG

**On macOS:**
```bash
brew install gnupg
```

**On Linux (Ubuntu/Debian):**
```bash
sudo apt-get install gnupg
```

**On Windows:**
Download and install from https://www.gnupg.org/download/

### Step 2: Generate GPG Key Pair

1. Generate a new GPG key:
```bash
gpg --gen-key
```

2. Follow the prompts:
   - Choose RSA and RSA (default)
   - Key size: 4096 bits
   - Expiration: 0 (key does not expire) or set an expiration date
   - Enter your name and email (use the same email as in pom.xml)
   - Set a strong passphrase (remember this!)

3. List your keys to get the key ID:
```bash
gpg --list-secret-keys --keyid-format=long
```

Example output:
```
sec   rsa4096/ABCD1234EFGH5678 2024-01-15 [SC]
```
The key ID is `ABCD1234EFGH5678`

4. Publish your public key to a key server:
```bash
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
```

Also publish to other key servers:
```bash
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
gpg --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
```

### Step 3: Create Maven Central Account

1. Go to https://central.sonatype.com
2. Click "Sign Up" and create an account (you can use GitHub to sign up)
3. Once logged in, click on your profile and verify your namespace:
   - For GitHub: `io.github.karthik-tarento`
   - You'll need to verify ownership by creating a repository named after the verification token
4. After namespace verification, generate a User Token:
   - Go to Account → View Account
   - Click "Generate User Token"
   - Save the username and password shown

### Step 4: Configure Maven Settings

Edit or create `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_CENTRAL_TOKEN_USERNAME</username>
      <password>YOUR_CENTRAL_TOKEN_PASSWORD</password>
    </server>
  </servers>

  <profiles>
    <profile>
      <id>gpg</id>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>YOUR_GPG_PASSPHRASE</gpg.passphrase>
      </properties>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>gpg</activeProfile>
  </activeProfiles>
</settings>
```

Replace:
- `YOUR_CENTRAL_TOKEN_USERNAME` with the token username from Step 3
- `YOUR_CENTRAL_TOKEN_PASSWORD` with the token password from Step 3
- `YOUR_GPG_PASSPHRASE` with your GPG key passphrase from Step 2

**Security Note:** Consider using Maven password encryption for sensitive data. See: https://maven.apache.org/guides/mini/guide-encryption.html

## Publishing a Release

### Option A: Manual Upload via Central Portal (Recommended for beginners)

1. **Build and sign the artifacts:**
   ```bash
   mvn clean verify
   ```

   This will create the following files in `target/`:
   - `common-util-1.0.0.jar` (main JAR)
   - `common-util-1.0.0-sources.jar` (sources)
   - `common-util-1.0.0-javadoc.jar` (javadoc)
   - `common-util-1.0.0.pom` (POM file)
   - `.asc` files for each (GPG signatures)

2. **Create a bundle for upload:**
   ```bash
   cd target
   jar -cvf bundle.jar common-util-1.0.0.jar common-util-1.0.0.jar.asc \
                       common-util-1.0.0-sources.jar common-util-1.0.0-sources.jar.asc \
                       common-util-1.0.0-javadoc.jar common-util-1.0.0-javadoc.jar.asc \
                       common-util-1.0.0.pom common-util-1.0.0.pom.asc
   ```

3. **Upload to Central Portal:**
   - Go to https://central.sonatype.com
   - Log in
   - Click "Publish" → "Upload Bundle"
   - Upload the `bundle.jar` file
   - Click "Publish" to deploy to Maven Central

4. **Wait for sync:**
   - Initial publishing may take 10-30 minutes
   - The artifact will appear on Maven Central within 1-2 hours
   - Search at https://search.maven.org for `io.github.karthik-tarento:common-util`

### Option B: Automated Maven Deploy (Advanced)

**Note:** This option requires additional POM configuration (Nexus Staging Plugin). For simplicity, Option A is recommended.

If you want to automate the process:

1. Add to your `pom.xml`:
   ```xml
   <distributionManagement>
     <repository>
       <id>central</id>
       <url>https://central.sonatype.com/api/v1/publisher</url>
     </repository>
   </distributionManagement>
   ```

2. Deploy:
   ```bash
   mvn clean deploy
   ```

## Pre-Release Checklist

Before publishing a new version:

- [ ] Update version in `pom.xml` (remove `-SNAPSHOT`)
- [ ] Update version in `README.md`
- [ ] Run tests: `mvn test`
- [ ] Build successfully: `mvn clean verify`
- [ ] Review generated artifacts in `target/`
- [ ] Verify GPG signatures exist (`.asc` files)
- [ ] Update changelog/release notes if applicable

## Post-Release Steps

1. **Tag the release in Git:**
   ```bash
   git tag -a v1.0.0 -m "Release version 1.0.0"
   git push origin v1.0.0
   ```

2. **Create GitHub Release:**
   - Go to your GitHub repository
   - Click "Releases" → "Create a new release"
   - Select the tag you just created
   - Add release notes
   - Publish release

3. **Update version for next development cycle:**
   - Update `pom.xml` version to next SNAPSHOT (e.g., `1.0.1-SNAPSHOT`)
   - Commit and push

## Troubleshooting

### GPG Issues

**Problem:** "gpg: signing failed: Inappropriate ioctl for device"
```bash
export GPG_TTY=$(tty)
```

**Problem:** Can't find GPG key
```bash
gpg --list-secret-keys --keyid-format=long
```

### Maven Build Issues

**Problem:** Tests failing
```bash
mvn clean verify -DskipTests
```
(Only use this if you're certain tests are not critical)

**Problem:** Javadoc errors
Check `pom.xml` line 144 - we have `<doclint>none</doclint>` to allow flexible javadoc

### Central Portal Issues

**Problem:** Namespace not verified
- Check your namespace verification status at https://central.sonatype.com
- Follow the instructions to create the verification repository

**Problem:** Upload rejected
- Ensure all required files are present: JAR, sources, javadoc, POM, and all .asc signatures
- Verify POM contains all required metadata (name, description, url, license, developers, scm)

## Verification

After publishing, verify your artifact:

1. **Check Maven Central Search:**
   https://search.maven.org/artifact/io.github.karthik-tarento/common-util

2. **Test in a sample project:**
   ```xml
   <dependency>
       <groupId>io.github.karthik-tarento</groupId>
       <artifactId>common-util</artifactId>
       <version>1.0.0</version>
   </dependency>
   ```

3. **Run:**
   ```bash
   mvn clean install
   ```

## Support

- Maven Central Portal: https://central.sonatype.com
- Documentation: https://central.sonatype.org/publish/
- Community Support: https://github.com/sonatype/central-publishing-maven-plugin

## Security Best Practices

1. Never commit `settings.xml` to version control
2. Use environment variables for sensitive data in CI/CD
3. Keep your GPG private key secure
4. Use a strong passphrase for your GPG key
5. Consider setting an expiration date for your GPG key
6. Backup your GPG keys securely

## Additional Resources

- [Maven Central Publishing Guide](https://central.sonatype.org/publish/)
- [GPG Quick Start](https://central.sonatype.org/publish/requirements/gpg/)
- [Maven Settings Encryption](https://maven.apache.org/guides/mini/guide-encryption.html)
