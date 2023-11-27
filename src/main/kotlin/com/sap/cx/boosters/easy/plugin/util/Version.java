package com.sap.cx.boosters.easy.plugin.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Version.
 */
public class Version implements Comparable<Version> {
    private static final Pattern PATTERN = Pattern.compile("(\\d)(\\.([1-9]?\\d))?");
    /**
     * The constant UNDEFINED_PART.
     */
    public static final int UNDEFINED_PART = Integer.MAX_VALUE;
    /**
     * The constant UNDEFINED.
     */
    public static final Version UNDEFINED = new Version(UNDEFINED_PART, UNDEFINED_PART, UNDEFINED_PART,
            "<undefined>");
    /**
     * The constant VERSION_COMPARATOR.
     */
    public static final Comparator<Version> VERSION_COMPARATOR = Comparator.comparingInt(Version::getMajor)
            .thenComparingInt(Version::getMinor).thenComparingInt(Version::getPatch);

    private final int major;
    private final int minor;
    private final int patch;
    private final String original;

    private Version(int major, int minor, int patch, String original) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.original = original;
    }

    private Version(int major, int minor, int patch, boolean preview, String original) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.original = original;
    }

    /**
     * Parse version version.
     *
     * @param versionString the version string
     * @return the version
     */
    public static Version parseVersion(String versionString) {
        return parseVersion(versionString, Collections.emptyMap());
    }

    /**
     * Parse version version.
     *
     * @param versionString          the version string
     * @param previewToPlatformPatch the preview to platform patch
     * @return the version
     */
    public static Version parseVersion(String versionString, Map<String, Integer> previewToPlatformPatch) {
        Objects.requireNonNull(versionString);

        Matcher oldV = PATTERN.matcher(versionString);
        Matcher newV = PATTERN.matcher(versionString);

        if (newV.matches()) {
            int patch = UNDEFINED_PART;

            if (newV.groupCount() > 3 && newV.group(4) != null) {
                patch = Integer.parseInt(newV.group(4));
            }
            return new Version(Integer.parseInt(newV.group(1)), Integer.parseInt(newV.group(2)), 0,
                    versionString);
        } else if (oldV.matches()) {
            int patch = UNDEFINED_PART;
            if (oldV.groupCount() > 4 && oldV.group(5) != null) {
                patch = Integer.parseInt(oldV.group(5));
            }
            return new Version(Integer.parseInt(oldV.group(1)), Integer.parseInt(oldV.group(2)),
                    Integer.parseInt(oldV.group(3)), versionString);
        }
        String[] split = versionString.split("\\.");
        int major = UNDEFINED_PART;
        int minor = UNDEFINED_PART;
        int patch = UNDEFINED_PART;
        switch (split.length) {
            case 4:
                patch = Integer.parseInt(split[3]);
            case 2:
                minor = Integer.parseInt(split[1]);
            case 1:
                major = Integer.parseInt(split[0]);
                break;
            default:
                throw new IllegalArgumentException("Could not parse " + versionString);
        }
        return new Version(major, minor, patch, versionString);
    }

    /**
     * Without patch version.
     *
     * @return the version
     */
    public Version withoutPatch() {
        return new Version(major, minor, UNDEFINED_PART, original);
    }

    @Override
    public int compareTo(Version o) {
        return VERSION_COMPARATOR.compare(this, o);
    }

    /**
     * Equals ignore patch boolean.
     *
     * @param version the version
     * @return the boolean
     */
    public boolean equalsIgnorePatch(Version version) {
        if (this == version) {
            return true;
        }
        return major == version.major && minor == version.minor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Version version = (Version) o;
        return major == version.major && minor == version.minor && patch == version.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    @Override
    public String toString() {
        return this.original;
    }

    /**
     * Gets major.
     *
     * @return the major
     */
    public int getMajor() {
        return major;
    }

    /**
     * Gets minor.
     *
     * @return the minor
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets patch.
     *
     * @return the patch
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Gets dependency version.
     *
     * @return the dependency version
     */
    public String getDependencyVersion() {
        String v = this.original;
        if (this.getPatch() == UNDEFINED_PART) {
            if (!v.endsWith(".")) {
                v += ".";
            }
            v += "+";
        }
        return v;
    }

}
