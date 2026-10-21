package ma.ensias.employe.demo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

// ⚠️ CODE VOLONTAIREMENT VULNÉRABLE — uniquement pour tester SonarCloud
@RestController
@RequestMapping("/demo")
public class DemoVulnerableController {

    // Vulnérabilité 3 : mot de passe en dur (CWE-798)
    private static final String ADMIN_PASSWORD = "Admin@2026!";

    private final JdbcTemplate jdbcTemplate;

    public DemoVulnerableController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Vulnérabilité 1 : injection SQL (CWE-89)
    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String name) {
        String sql = "SELECT * FROM employe WHERE name = '" + name + "'";
        return jdbcTemplate.queryForList(sql);
    }

    // Vulnérabilité 2 : injection de commande système (CWE-78)
    @GetMapping("/ping")
    public String ping(@RequestParam String host) throws Exception {
        Process p = Runtime.getRuntime().exec("ping -n 1 " + host);
        return new String(p.getInputStream().readAllBytes());
    }

    // Vulnérabilité 4 : chiffrement faible, DES en mode ECB (CWE-327)
    @GetMapping("/cipher")
    public String cipher() throws Exception {
        Cipher c = Cipher.getInstance("DES/ECB/PKCS5Padding");
        return c.getAlgorithm();
    }

    // Vulnérabilité 5 : hachage faible, MD5 (CWE-328)
    @GetMapping("/hash")
    public String hash(@RequestParam String value) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return HexFormat.of().formatHex(md.digest(value.getBytes()));
    }

    // Utilise le mot de passe en dur
    @GetMapping("/admin")
    public String admin(@RequestParam String password) {
        return ADMIN_PASSWORD.equals(password) ? "Accès admin OK" : "Refusé";
    }
}