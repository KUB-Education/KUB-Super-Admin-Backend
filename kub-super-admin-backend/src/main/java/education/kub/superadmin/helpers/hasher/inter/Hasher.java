package education.kub.superadmin.helpers.hasher;

public interface IHasher {
    public String createHash(String string);
    public boolean checkHash(String rawString, String hashedString);
}
