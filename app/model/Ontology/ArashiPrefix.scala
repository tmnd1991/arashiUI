package model.Ontology

import org.w3.banana.{PrefixBuilder, XSDPrefix, RDFOps, RDF}

/**
 * Created by Antonio on 04/03/2015.
 */
object ArashiPrefix {
  def apply[Rdf <: RDF](implicit ops: RDFOps[Rdf]) = new ArashiPrefix[Rdf](ops)
}

class ArashiPrefix[Rdf <: RDF](ops: RDFOps[Rdf]) extends PrefixBuilder("smacs", "http://ing.unibo.it/smacs/predicates#")(ops) {
  import ops._
  val name                   = apply("name")
  val memoryUsage            = apply("hasMemoryUsage")
  val memoryUsagePercentage  = apply("hasMemoryUsagePercentage")
  val averageLoad            = apply("averageLoad")
  val minLoad                = apply("minLoad")
  val maxLoad                = apply("maxLoad")
  val swapUsage              = apply("swapUsage")
  val swapUsagePercentage    = apply("swapUsagePercentage")
  val cpuUsage               = apply("cpuUsage")
  val cpuUsageUser           = apply("cpuUsageUser")
  val cpuUsageSystem         = apply("cpuUsageSystem")
  val cpuUsageWait           = apply("cpuUsageWait")
  val status                 = apply("status")
  val monitoringStatus       = apply("monitoringStatus")
  val pid                    = apply("pid")
  val parentPid              = apply("parentPid")
  val uptimeMs               = apply("uptimeMs")
  val children               = apply("children")
  val memoryKbUsage          = apply("memoryKbUsage")
  val totalMemoryKb          = apply("totalMemoryKb")
  val memoryPercUsage        = apply("memoryPercUsage")
  val totalMemoryPerc        = apply("totalMemoryPerc")
  val totalCPUperc           = apply("totalCPUperc")
  val dataCollected          = apply("dataCollected")
  val portResponseTime       = apply("portResponseTime")
  val portResponseDestination= apply("portResponseDestination")
  val portResponseMode       = apply("portResponseMode")
  val unixSocketResponseTime = apply("unixSocketResponseTime")
  val unixSocketResponseDestination = apply("unixSocketResponseDestination")
  val unixSocketResponseMode = apply("unixSocketResponseMode")
  val cpuName                = apply("cpuName")
  val percentageUsage     = apply("percentageUsage")
  val bytesReadFromDisk      = apply("bytesReadFromDisk")
  val readsFromDisk          = apply("readsFromDisk")
  val bytesWroteToDisk       = apply("bytesWroteToDisk")
  val writesToDisk           = apply("writesToDisk")
  val freeMemPercentage      = apply("freeMemPercentage")
  val bytesReadFromNet       = apply("bytesReadFromNet")
  val bytesWroteToNet        = apply("bytesWroteToNet")
  val coreNumber             = apply("coreNumber")
  val osName                 = apply("osName")
  val numberOfProcesses      = apply("numberOfProcesses")
  val sampleType             = apply("sampleType")
  val sampleId               = apply("sampleId")
  val projectId              = apply("projectId")
  val recordedAt             = apply("recordedAt")
  val resourceId             = apply("resourceId")
  val source                 = apply("source")
  val timestamp              = apply("timestamp")
  val unit                   = apply("unit")
  val userId                 = apply("userId")
  val volume                 = apply("volume")
  val averageValue           = apply("averageValue")
  val countValue             = apply("countValue")
  val duration               = apply("duration")
  val durationEnd            = apply("durationEnd")
  val durationStart          = apply("durationStart")
  val maxValue               = apply("maxValue")
  val minValue               = apply("minValue")
  val period                 = apply("period")
  val periodEnd              = apply("periodEnd")
  val periodStart            = apply("periodStart")
  val sumValue               = apply("sumValue")
  val counterName            = apply("counterName")
  val counterType            = apply("counterType")
  val counterUnit            = apply("counterUnit")
  val counterVolume          = apply("counterVolume")
  val messageId              = apply("messageId")
  val id                     = apply("id")
  val resourceType           = apply("resource")
  def newProperty(s : String) = apply(s)
  // http://www.w3.org/TR/owl-rdf-based-semantics
  // Table 3.3 Datatypes of the OWL 2 RDF-Based Semantics

  // http://www.w3.org/TR/owl2-syntax/
  // Table 3 Reserved VOcabulary of OWL 2 with Special Treatment
}
