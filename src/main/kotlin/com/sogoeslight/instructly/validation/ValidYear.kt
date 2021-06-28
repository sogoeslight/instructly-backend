package com.sogoeslight.instructly.validation

import java.time.Year
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

annotation class ValidYear

class ValidYearValidator : ConstraintValidator<ValidYear, Int> {

    override fun isValid(value: Int, context: ConstraintValidatorContext?): Boolean =
        value in 1991..Year.now().value
}