public enum Residency {
    MENA_LTR,       // Local Talent Rule: legal resident of a MENA country (Egypt, Jordan, KSA, UAE, etc.)
    EMEA_RESIDENT,  // Legal resident (6+ months) or citizen of an EMEA country, outside MENA
    OTHER;          // Not a resident of the EMEA territory at all

    // Geographically, a MENA LTR player is also an EMEA resident, so they
    // count toward both the "EMEA Residents" and the "MENA LTR" minimums.
    public boolean countsAsEmeaResident() {
        return this == MENA_LTR || this == EMEA_RESIDENT;
    }
}
