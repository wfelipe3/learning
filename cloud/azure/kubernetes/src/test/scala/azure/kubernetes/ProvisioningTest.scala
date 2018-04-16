package azure.kubernetes

import java.io.File

import cats.data.Validated.{Invalid, Valid}
import cats.data.{Reader, Validated}
import com.microsoft.azure.management.Azure
import com.microsoft.azure.management.compute.{ContainerServiceMasterProfileCount, ContainerServiceVMSizeTypes}
import com.microsoft.azure.management.resources.ResourceGroup
import com.microsoft.azure.management.resources.fluentcore.arm.Region
import org.scalatest.{fixture, _}

/**
  * Created by feliperojas on 11/26/17.
  */
class ProvisioningTest extends fixture.FreeSpec with Matchers with fixture.ConfigMapFixture {

  object Delete extends Tag("delete")

  object Create extends Tag("create")

  "provision kubernetes" taggedAs Create in { cm =>
    val value = getAzureAuth(cm)
      .map(azure)
      .map {
        createResourceGroup("felipekubertest", Region.US_EAST)
          .flatMap(createKubernetes)
          .run
      }

    value should be(Valid("test"))
  }

  "delete resource group" taggedAs Delete in { cm =>
    val value = getAzureAuth(cm)
      .map(azure)
      .map(deleteResourceGroup("felipekubertest").run)

    value should be(Valid(()))
  }

  private def azure(file: File) =
    Azure.authenticate(file)
      .withDefaultSubscription()

  private def deleteResourceGroup(name: String): Reader[Azure, Unit] = Reader { azure =>
    azure.resourceGroups().deleteByName(name)
  }

  private def createResourceGroup(name: String, region: Region): Reader[Azure, ResourceGroup] = Reader { azure =>
    azure.resourceGroups()
      .define(name)
      .withRegion(region)
      .create()
  }

  def createKubernetes(rg: ResourceGroup): Reader[Azure, Any] = Reader { azure =>
    azure.containerServices()
      .define("bizagi-cs")
      .withRegion(Region.US_EAST)
      .withExistingResourceGroup(rg)
      .withKubernetesOrchestration()
      .withServicePrincipal("", "")
      .withLinux()
      .withRootUsername("bizagi")
      .withSshKey("")
      .withMasterNodeCount(ContainerServiceMasterProfileCount.MIN)
      .withMasterLeafDomainLabel("bizagi-dns-test")
      .defineAgentPool("bizagi-agentpool")
      .withVMCount(1)
      .withVMSize(ContainerServiceVMSizeTypes.STANDARD_D1_V2)
      .withLeafDomainLabel("bizagi-dns-ap-test")
      .attach()
      .create()
  }

  private def getAzureAuth(configMap: ConfigMap): Validated[String, File] =
    configMap.get("azureAuth")
      .map(a => new File(a.asInstanceOf[String]))
      .filter(_.exists())
      .map(Valid(_))
      .getOrElse(Invalid("No azure auth found"))

}
