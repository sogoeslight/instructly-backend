package com.sogoeslight.instructly.annotations

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:populateWithTestData.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
annotation class IntegrationTest
