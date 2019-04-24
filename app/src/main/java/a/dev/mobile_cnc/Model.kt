package a.dev.mobile_cnc




enum class Model private constructor(val titleResId: Int, val layoutResId: Int) {
    a(R.string.one, R.layout.layout_formula_find_v),
    b(R.string.two, R.layout.layout_formula_find_ra_rz),
    c(R.string.three, R.layout.layout_three)
}