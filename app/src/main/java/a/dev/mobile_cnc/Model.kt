package a.dev.mobile_cnc




enum class Model(val titleResId: Int, val layoutResId: Int) {
    FIND_V(R.string.v_title, R.layout.layout_formula_find_v),
    FIND_FEED_MILL(R.string.mill_title, R.layout.layout_formula_feed_mill),
    FIND_DIMENSION(R.string.dim_title, R.layout.layout_formula_razmer),
    FIND_LENGTH_ANGLE(R.string.drill_title, R.layout.layout_formula_length_angle),
    FIND_RA_RZ(R.string.formula_find_ra_rz, R.layout.layout_formula_find_ra_rz)
}