package education.kub.superadmin.helpers.hasher.inter;

public interface Hasher {
    public String createHash(String string);
    public boolean checkHash(String rawString, String hashedString);
}
