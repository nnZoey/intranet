package vn.leap.intranet;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("vn.leap.intranet");

        noClasses()
            .that()
            .resideInAnyPackage("vn.leap.intranet.service..")
            .or()
            .resideInAnyPackage("vn.leap.intranet.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..vn.leap.intranet.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
