package com.jarvis.assist.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Vault — AES-256-GCM encrypted local password manager.
 * Keys never leave the Android Keystore hardware.
 * Mirrors AugmaOS GetVault / VaultLink pattern from DEX.
 */
public class Vault {

    private static final String ALIAS    = "JarvisVaultKey_v1";
    private static final String PREFS    = "jarvis_vault_v2";
    private static final String IDX_KEY  = "__vault_index__";
    private static final int    GCM_TAG  = 128;

    public static class Entry {
        public String id, label, username, encPw, category, notes;
        public boolean revealed;
    }

    // ── Crypto ────────────────────────────────────────────────────────────────

    private static SecretKey key() throws Exception {
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        if (ks.containsAlias(ALIAS)) {
            return ((KeyStore.SecretKeyEntry) ks.getEntry(ALIAS, null)).getSecretKey();
        }
        KeyGenerator kg = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        kg.init(new KeyGenParameterSpec.Builder(ALIAS,
            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(false)
            .build());
        return kg.generateKey();
    }

    public static String encrypt(String plain) {
        try {
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key());
            byte[] iv   = c.getIV();
            byte[] ct   = c.doFinal(plain.getBytes(StandardCharsets.UTF_8));
            byte[] out  = new byte[iv.length + ct.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(ct, 0, out, iv.length, ct.length);
            return Base64.encodeToString(out, Base64.NO_WRAP);
        } catch (Exception e) { return null; }
    }

    public static String decrypt(String enc) {
        try {
            byte[] all = Base64.decode(enc, Base64.NO_WRAP);
            byte[] iv  = new byte[12];
            byte[] ct  = new byte[all.length - 12];
            System.arraycopy(all, 0, iv, 0, 12);
            System.arraycopy(all, 12, ct, 0, ct.length);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, key(), new GCMParameterSpec(GCM_TAG, iv));
            return new String(c.doFinal(ct), StandardCharsets.UTF_8);
        } catch (Exception e) { return null; }
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public static List<Entry> load(Context ctx) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String idx = p.getString(IDX_KEY, "");
        List<Entry> out = new ArrayList<>();
        if (idx.isEmpty()) return out;
        for (String id : idx.split(",")) {
            if (id.isEmpty()) continue;
            Entry e = new Entry();
            e.id       = id;
            e.label    = p.getString(id + "_l", "");
            e.username = p.getString(id + "_u", "");
            e.encPw    = p.getString(id + "_p", "");
            e.category = p.getString(id + "_c", "Other");
            e.notes    = p.getString(id + "_n", "");
            out.add(e);
        }
        return out;
    }

    public static Entry save(Context ctx, String label, String user,
                             String pw, String cat, String notes) {
        String enc = encrypt(pw);
        if (enc == null) return null;
        String id = "v" + System.currentTimeMillis();
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putString(id + "_l", label);
        ed.putString(id + "_u", user);
        ed.putString(id + "_p", enc);
        ed.putString(id + "_c", cat.isEmpty() ? "Other" : cat);
        ed.putString(id + "_n", notes);
        String existing = p.getString(IDX_KEY, "");
        ed.putString(IDX_KEY, existing.isEmpty() ? id : existing + "," + id);
        ed.apply();
        Entry e = new Entry();
        e.id = id; e.label = label; e.username = user;
        e.encPw = enc; e.category = cat; e.notes = notes;
        return e;
    }

    public static void delete(Context ctx, String id) {
        SharedPreferences p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.remove(id + "_l"); ed.remove(id + "_u");
        ed.remove(id + "_p"); ed.remove(id + "_c"); ed.remove(id + "_n");
        String idx = p.getString(IDX_KEY, "");
        List<String> ids = new ArrayList<>();
        for (String s : idx.split(",")) if (!s.equals(id) && !s.isEmpty()) ids.add(s);
        ed.putString(IDX_KEY, String.join(",", ids));
        ed.apply();
    }
}
