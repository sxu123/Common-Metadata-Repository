;; WARNING: This file was generated from umm-s-json-schema.json. Do not manually modify.
(ns cmr.umm-spec.models.umm-service-models
   "Defines UMM-S clojure records."
 (:require [cmr.common.dev.record-pretty-printer :as record-pretty-printer]))

(defrecord UMM-S
  [
   ;; This field provides users with information on what changes were included in the most recent
   ;; version.
   VersionDescription

   ;; The service provider, or organization, or institution responsible for developing, archiving,
   ;; and/or distributing the service, software, or tool.
   ServiceOrganizations

   ;; This element contains important information about the Unique Resource Locator for the service.
   ServiceOptions

   ;; This is the contact persons of the service.
   ContactPersons

   ;; Information about any constraints for accessing the service, software, or tool.
   AccessConstraints

   ;; This is the contact groups of the service.
   ContactGroups

   ;; Allows for the specification of Earth Science Service keywords that are representative of the
   ;; service, software, or tool being described. The controlled vocabulary for Service Keywords is
   ;; maintained in the Keyword Management System (KMS).
   ServiceKeywords

   ;; This element describes the latest date when the service was most recently pushed to production
   ;; for support and maintenance.
   LastUpdatedDate

   ;; Information on how the item (service, software, or tool) may or may not be used after access
   ;; is granted. This includes any special restrictions, legal prerequisites, terms and conditions,
   ;; and/or limitations on using the item. Providers may request acknowledgement of the item from
   ;; users and claim no responsibility for quality and completeness.
   UseConstraints

   ;; The name of the service, software, or tool.
   Name

   ;; A brief description of the service.
   Description

   ;; This class describes the signature of the operational metadata provided by the service.
   OperationMetadata

   ;; The type of the service, software, or tool.
   Type

   ;; Words or phrases to further describe the service, software, or tool.
   AncillaryKeywords

   ;; This element contains important information about the universal resource locator (URL) for the
   ;; service.
   URL

   ;; The edition or version of the service.
   Version

   ;; Information about the quality of the service, software, or tool, or any quality assurance
   ;; procedures followed in development.
   ServiceQuality

   ;; The long name of the service, software, or tool.
   LongName
  ])
(record-pretty-printer/enable-record-pretty-printing UMM-S)

(defrecord TimePointsType
  [
   ;; Time format representing time point of the temporal extent.
   TimeFormat

   ;; Time value of the time point of temporal extent.
   TimeValue

   ;; Description of the time value of the temporal extent.
   Description
  ])
(record-pretty-printer/enable-record-pretty-printing TimePointsType)

;; Defines the contact information of a service organization or service contact.
(defrecord ContactInformationType
  [
   ;; A URL associated with the contact, e.g., the home page for the service provider which is
   ;; responsible for the service.
   OnlineResources

   ;; Time period when the contact answers questions or provides services.
   ServiceHours

   ;; Supplemental instructions on how or when to contact the responsible party.
   ContactInstruction

   ;; Mechanisms of contacting.
   ContactMechanisms

   ;; Contact addresses.
   Addresses
  ])
(record-pretty-printer/enable-record-pretty-printing ContactInformationType)

(defrecord ContactGroupType
  [
   ;; This is the roles of the service contact.
   Roles

   ;; This is the contact information of the service contact.
   ContactInformation

   ;; This is the contact group name.
   GroupName
  ])
(record-pretty-printer/enable-record-pretty-printing ContactGroupType)

;; Defines a service organization which is either an organization or institution responsible for
;; distributing, archiving, or processing the data via a service, etc.
(defrecord ServiceOrganizationType
  [
   ;; This is the roles of the service organization.
   Roles

   ;; This is the short name of the service organization.
   ShortName

   ;; This is the long name of the service organization.
   LongName

   ;; This is the URL of the service organization.
   OnlineResource
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceOrganizationType)

;; The general grid consists of a CRS Identifier and an Axis.
(defrecord GeneralGridType
  [
   ;; The CRS identifier (srsName) of the general grid.
   CRSIdentifier

   ;; The grid axis identifiers, all distinct within a grid.
   Axis
  ])
(record-pretty-printer/enable-record-pretty-printing GeneralGridType)

;; This element contains the name of the chained operation(s) made possible via this service.
(defrecord OperationChainedMetadataType
  [
   ;; TThis element contains the name of the operation chain made possible via this service.
   OperationChainName

   ;; This element contains the description of the operation chain made possible via this service.
   OperationChainDescription
  ])
(record-pretty-printer/enable-record-pretty-printing OperationChainedMetadataType)

;; Enables specification of Earth science service keywords related to the service. The Earth Science
;; Service keywords are chosen from a controlled keyword hierarchy maintained in the Keyword
;; Management System (KMS).
(defrecord ServiceKeywordType
  [
   ServiceCategory

   ServiceTopic

   ServiceTerm

   ServiceSpecificTerm
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceKeywordType)

(defrecord DataResourceSpatialExtentType
  [
   ;; The spatial extent of the layer, feature type or coverage described by a point.
   SpatialPoints

   ;; The spatial extent of the layer, feature type or coverage described by a line string.
   SpatialLineStrings

   ;; The spatial extent of the layer, feature type or coverage described by a bounding box.
   SpatialBoundingBox

   ;; The spatial extent of the layer, feature type or coverage described by a general grid.
   GeneralGrid

   ;; The spatial extent of the layer, feature type or coverage described by a polygon.
   SpatialPolygons
  ])
(record-pretty-printer/enable-record-pretty-printing DataResourceSpatialExtentType)

;; The bounding box consists of west bounding, south bounding, east bounding and north bounding
;; coordinates and the CRS identifier.
(defrecord SpatialBoundingBoxType
  [
   ;; The CRS identifier of the bounding box.
   CRSIdentifier

   ;; The west bounding coordinate of the bounding box.
   WestBoundingCoordinate

   ;; The south bounding coordinate of the bounding box.
   SouthBoundingCoordinate

   ;; The east bounding coordinate of the bounding box.
   EastBoundingCoordinate

   ;; The north bounding coordinate of the bounding box.
   NorthBoundingCoordinate
  ])
(record-pretty-printer/enable-record-pretty-printing SpatialBoundingBoxType)

;; This element contains the name of the chained operation(s) made possible via this service.
(defrecord CoupledResourceType
  [
   ;; This element contains the name of the resource(s) coupled to this service.
   ScopedName

   ;; This element contains the DOI for the resource(s) coupled to this service.
   DataResourceDOI

   ;; This element contains the data identification and scope for the resource(s) coupled to this
   ;; service.
   DataResource

   ;; This element contains the coupling type for the resource(s) coupled to this service.
   CouplingType
  ])
(record-pretty-printer/enable-record-pretty-printing CoupledResourceType)

;; Method for contacting the service contact. A contact can be available via phone, email, Facebook,
;; or Twitter.
(defrecord ContactMechanismType
  [
   ;; This is the method type for contacting the responsible party - phone, email, Facebook, or
   ;; Twitter.
   Type

   ;; This is the contact phone number, email address, Facebook address, or Twitter handle
   ;; associated with the contact method.
   Value
  ])
(record-pretty-printer/enable-record-pretty-printing ContactMechanismType)

;; The extent consists of .
(defrecord ExtentType
  [
   ;; The label of the extent.
   ExtentLabel

   ;; The lowest value along this grid axis.
   LowerBound

   ;; The highest value along this grid axis.
   UpperBound

   ;; The unit of measure in which values along this axis are expressed.
   UOMLabel
  ])
(record-pretty-printer/enable-record-pretty-printing ExtentType)

;; This object describes service quality, composed of the quality flag, the quality flagging system,
;; traceability and lineage.
(defrecord ServiceQualityType
  [
   ;; The quality flag for the service.
   QualityFlag

   ;; The quality traceability of the service.
   Traceability

   ;; The quality lineage of the service.
   Lineage
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceQualityType)

;; This element is used to identify the input projection type of the variable.
(defrecord SupportedProjectionType
  [
   ;; This element is used to identify the list of supported projection types.
   ProjectionName

   ;; This element is used to identify the origin of the x-coordinates at the center of the
   ;; projection.
   ProjectionLatitudeOfCenter

   ;; This element is used to identify the origin of the y-coordinates at the center of the
   ;; projection.
   ProjectionLongitudeOfCenter

   ;; This element is used to identify the linear value applied to the origin of the y-coordinates.
   ;; False easting and northing values are usually applied to ensure that all the x and y values
   ;; are positive.
   ProjectionFalseEasting

   ;; This element is used to identify the linear value applied to the origin of the x-coordinates.
   ;; False easting and northing values are usually applied to ensure that all the x and y values
   ;; are positive.
   ProjectionFalseNorthing

   ;; This element is used to identify the authority, expressed as the authority code, for the list
   ;; of supported projection types.
   ProjectionAuthority

   ;; This element is used to identify the projection unit of measurement.
   ProjectionUnit

   ;; This element is used to identify the projection datum name.
   ProjectionDatumName
  ])
(record-pretty-printer/enable-record-pretty-printing SupportedProjectionType)

;; The line string consists of two points: a start point and an end ppint.
(defrecord LineStringType
  [
   ;; The start point of the line string.
   StartPoint

   ;; The end point of the line string.
   EndPoint
  ])
(record-pretty-printer/enable-record-pretty-printing LineStringType)

(defrecord DataResourceTemporalExtentType
  [
   ;; Points in time representing the temporal extent of the layer or coverage.
   DataResourceTimePoints
  ])
(record-pretty-printer/enable-record-pretty-printing DataResourceTemporalExtentType)

;; The DataResource class describes the layers, feature types or coverages available from the
;; service.
(defrecord DataResourceType
  [
   ;; The resource type of the layer, feature type or coverage available from the service.
   DataResourceSourceType

   ;; The temporal extent of the layer, feature type or coverage available from the service.
   DataResourceTemporalExtent

   ;; The identifier of the layer, feature type or coverage available from the service.
   DataResourceIdentifier

   ;; TThe temporal resolution of the layer, feature type or coverage available from the service.
   TemporalResolution

   ;; Path relative to the root URL for the layer, feature type or coverage service.
   RelativePath

   ;; The unit of the temporal resolution of the layer, feature type or coverage available from the
   ;; service.
   TemporalResolutionUnit

   ;; The spatial extent of the layer, feature type or coverage available from the service.
   DataResourceSpatialType

   ;; The spatial resolution of the layer, feature type or coverage available from the service.
   SpatialResolution

   ;; The spatial extent of the coverage available from the service. These are coordinate pairs
   ;; which describe either the point, line string, or polygon representing the spatial extent. The
   ;; bounding box is described by the west, south, east and north ordinates
   DataResourceSpatialExtent

   ;; The unit of the spatial resolution of the layer, feature type or coverage available from the
   ;; service.
   SpatialResolutionUnit

   ;; The temporal extent of the layer, feature type or coverage available from the service.
   DataResourceTemporalType
  ])
(record-pretty-printer/enable-record-pretty-printing DataResourceType)

;; This class describes the signature of the operational metadata provided by the service.
(defrecord OperationMetadataType
  [
   ;; This element contains the name of the operation(s) made possible via this service.
   OperationName

   ;; This element contains the distributed computing platform (protocol) for the operation(s) made
   ;; possible via this service.
   DistributedComputingPlatform

   ;; This element contains the description of the operation(s) made possible via this service.
   OperationDescription

   ;; This element contains the name of the invocation of the operation(s) made possible via this
   ;; service.
   InvocationName

   ;; This element contains the URL of the invocation of the operation(s) made possible via this
   ;; service.
   ConnectPoint

   ;; This element contains the name of the chained operation(s) made possible via this service.
   OperationChainedMetadata

   ;; This element contains important information about the resource(s) coupled to this service.
   CoupledResource

   ;; This element contains important information about the parameter associated with the
   ;; resource(s) coupled to this service.
   Parameters
  ])
(record-pretty-printer/enable-record-pretty-printing OperationMetadataType)

;; The axis consists of an extent and grid information.
(defrecord AxisType
  [
   ;; The axis label of the general grid.
   AxisLabel

   ;; The resolution of the general grid.
   GridResolution

   ;; The extent of the general grid.
   Extent
  ])
(record-pretty-printer/enable-record-pretty-printing AxisType)

(defrecord ContactPersonType
  [
   ;; This is the roles of the service contact.
   Roles

   ;; This is the contact information of the service contact.
   ContactInformation

   ;; First name of the individual.
   FirstName

   ;; Middle name of the individual.
   MiddleName

   ;; Last name of the individual.
   LastName
  ])
(record-pretty-printer/enable-record-pretty-printing ContactPersonType)

;; Describes the online resource pertaining to the data.
(defrecord OnlineResourceType
  [
   ;; The URL of the website related to the online resource.
   Linkage

   ;; The protocol of the linkage for the online resource, such as https, svn, ftp, etc.
   Protocol

   ;; The application profile holds the name of the application that can service the data. For
   ;; example if the URL points to a word document, then the applicationProfile is MS-Word.
   ApplicationProfile

   ;; The name of the online resource.
   Name

   ;; The description of the online resource.
   Description

   ;; The function of the online resource. In ISO where this class originated the valid values are:
   ;; download, information, offlineAccess, order, and search.
   Function
  ])
(record-pretty-printer/enable-record-pretty-printing OnlineResourceType)

;; This element contains important information about the parameter associated with the resource(s)
;; coupled to this service.
(defrecord ParameterType
  [
   ;; This element contains the name of the parameter associated with the resource(s) coupled to
   ;; this service.
   ParameterName

   ;; This element contains the direction of the parameter associated with the resource(s) coupled
   ;; to this service.
   ParameterDirection

   ;; This element contains the description of the parameter associated with the resource(s) coupled
   ;; to this service.
   ParameterDescription

   ;; This element contains the optionality of the parameter associated with the resource(s) coupled
   ;; to this service
   ParameterOptionality

   ;; This element contains the repeatability of the parameter associated with the resource(s)
   ;; coupled to this service.
   ParameterRepeatability
  ])
(record-pretty-printer/enable-record-pretty-printing ParameterType)

;; This element contains the URL of the invocation of the operation(s) made possible via this
;; service.
(defrecord ConnectPointType
  [
   ;; This element contains the name of the resource(s) coupled to this service.
   ResourceName

   ;; This element contains the URL of the resource(s) coupled to this service.
   ResourceLinkage

   ;; This element contains the description of the resource(s) coupled to this service.
   ResourceDescription
  ])
(record-pretty-printer/enable-record-pretty-printing ConnectPointType)

;; This object describes service options, data transformations and output formats.
(defrecord ServiceOptionsType
  [
   ;; This element is used to identify the list of supported methods of variable aggregation.
   VariableAggregationSupportedMethods

   ;; This element is used to identify the list of supported subsetting requests.
   SubsetTypes

   ;; The project element describes the list of input format names supported by the service.
   SupportedInputFormats

   ;; The project element describes the list of format name combinations which explicitly state
   ;; which re-formatting options are available. These are entered as pairs of values, e.g. if
   ;; NetCDF-3 -> NetCDF-4 is a valid supported reformatting, these two values would be entered as a
   ;; pair.
   SupportedReformattings

   ;; This element is used to identify the list of supported input projections types.
   SupportedInputProjections

   ;; This element is used to identify the list of supported interpolation types.
   InterpolationTypes

   ;; This field indicates the maximum number of granules which this service can process with one
   ;; request.
   MaxGranules

   ;; This element is used to identify the list of supported output projections types.
   SupportedOutputProjections

   ;; The project element describes the list of output format names supported by the service.
   SupportedOutputFormats
  ])
(record-pretty-printer/enable-record-pretty-printing ServiceOptionsType)

;; This object describes the supported reformatting pairs, e.g. NetCDF4 -> [COG]. For every input
;; there is 1 or more outputs.
(defrecord SupportedReformattingsPairType
  [
   ;; This element is used to identify the name of the supported input format in the pair.
   SupportedInputFormat

   ;; This element is used to identify the name of all supported output formats for the provided
   ;; input format.
   SupportedOutputFormats
  ])
(record-pretty-printer/enable-record-pretty-printing SupportedReformattingsPairType)

;; This entity contains the physical address details for the contact.
(defrecord AddressType
  [
   ;; An address line for the street address, used for mailing or physical addresses of
   ;; organizations or individuals who serve as contacts for the service.
   StreetAddresses

   ;; The city portion of the physical address.
   City

   ;; The state or province portion of the physical address.
   StateProvince

   ;; The country of the physical address.
   Country

   ;; The zip or other postal code portion of the physical address.
   PostalCode
  ])
(record-pretty-printer/enable-record-pretty-printing AddressType)

;; The coordinates consist of a latitude and longitude.
(defrecord CoordinatesType
  [
   ;; The latitude of the point.
   Latitude

   ;; The longitude of the point.
   Longitude
  ])
(record-pretty-printer/enable-record-pretty-printing CoordinatesType)

;; Represents the Internet site where you can directly access the back-end service.
(defrecord URLType
  [
   ;; Description of the web page at this URL.
   Description

   ;; The URL for the relevant web page (e.g., the URL of the responsible organization's home page,
   ;; the URL of the collection landing page, the URL of the download site for the collection).
   URLValue
  ])
(record-pretty-printer/enable-record-pretty-printing URLType)

;; Information on how the item (service, software, or tool) may or may not be used after access is
;; granted. This includes any special restrictions, legal prerequisites, terms and conditions,
;; and/or limitations on using the item. Providers may request acknowledgement of the item from
;; users and claim no responsibility for quality and completeness.
(defrecord UseConstraintsType
  [
   ;; The web address of the license associated with the service.
   LicenseURL

   ;; The text of the license associated with the service.
   LicenseText
  ])
(record-pretty-printer/enable-record-pretty-printing UseConstraintsType)