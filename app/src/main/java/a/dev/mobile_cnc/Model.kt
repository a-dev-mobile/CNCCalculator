package a.dev.mobile_cnc




enum class Model private constructor(val titleResId: Int, val layoutResId: Int) {
    a(R.string.formula_find_v, R.layout.layout_formula_find_v),
    b(R.string.formula_find_ra_rz, R.layout.layout_formula_find_ra_rz),
    c(R.string.formula_razmer, R.layout.layout_formula_razmer),
    d(R.string.formula_length_angle, R.layout.layout_formula_length_angle)
}