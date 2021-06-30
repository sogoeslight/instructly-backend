package com.sogoeslight.instructly.validation

import java.time.Year
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Constraint(validatedBy = [ValidYearValidator::class])
@Target(allowedTargets = [FIELD])
annotation class ValidYear(
    val message: String = "Provided year is not in range 1991 - %current_year%+1",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class ValidYearValidator : ConstraintValidator<ValidYear, Int> {

    override fun isValid(value: Int, context: ConstraintValidatorContext?): Boolean =
        value in 1991..Year.now().value + 1

}