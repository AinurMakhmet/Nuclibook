package nuclibook.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@DatabaseTable(tableName = "staff")
public class Staff {
    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(width = 64)
    private String name;

    @DatabaseField(columnName = "role", foreign = true)
    private StaffRole role;

    @DatabaseField
    private String passwordHash;

    @DatabaseField
    private String passwordSalt;



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean checkPassword(String password) throws CannotHashPasswordException {
        // Add salt to password
        password = this.passwordSalt + password;

        // Check password
        return generateHash(password).equals(this.passwordHash);
    }

    public void setPassword(String password) throws CannotHashPasswordException {
        // Generate new random salt
        SecureRandom random = new SecureRandom();
        String salt = new BigInteger(130, random).toString(32);

        // Add salt to password
        password = salt + password;

        // Generate password hash
        String hash = generateHash(password);

        // Update fields
        this.passwordSalt = salt;
        this.passwordHash = hash;
    }

    private String generateHash(String text) throws CannotHashPasswordException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new CannotHashPasswordException("No such MessageDigest algorithm");
        }

        byte[] hash;
        try {
            hash = digest.digest(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new CannotHashPasswordException("Unsupported text encoding");
        }

        return String.format("%0128x", new BigInteger(1, hash));
    }
    
}
