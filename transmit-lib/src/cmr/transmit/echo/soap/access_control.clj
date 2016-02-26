(ns cmr.transmit.echo.soap.access-control
  "Helper to perform Access Control tasks against the SOAP API."
  (:require [cmr.transmit.echo.soap.core :as soap]
            [cmr.common.xml.parse :as xp]
            [cmr.common.xml.simple-xpath :as xpath]
            [cmr.common.log :refer (debug info warn error)]))

(defn set-permissions-request
  "Returns a hiccup representation of the SOAP body for a SetPermissions request using the provided parameters."
  [param-map]
  (let [{:keys [token acl-guid aces replace-all]} param-map]
    ["ns2:SetPermissions"
      {"xmlns:ns2" "http://echo.nasa.gov/echo/v10"
       "xmlns:ns3" "http://echo.nasa.gov/echo/v10/types"
       "xmlns:ns4" "http://echo.nasa.gov/ingest/v10"}
      ["ns2:token" token]
      ["ns2:aclGuid" acl-guid]
      ["ns2:aces"
        (for [ace aces] ace)]
      ["ns2:replaceAll" replace-all]]))

(defn set-permissions
  "Perform a SetPermissions request against the SOAP API."
  [param-map]
  (let [[status body-xml] (soap/post-soap :access_control
                            (set-permissions-request param-map))]
      (xp/value-of body-xml "/Envelope/Body/SetPermissionsResponse/result")))
