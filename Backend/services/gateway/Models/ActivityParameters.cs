using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace gateway.Models
{
    public class ActivityParameters
    {
        [JsonProperty("Duration")]
        public double? Duration { get; set; }

        [JsonProperty("Heart_Rate")]
        public double? Heart_Rate { get; set; }

        [JsonProperty("Body_Temp")]
        public double? Body_Temp { get; set; }

        [JsonProperty("userID")]
        public int? UserID { get; set; }

    }

    
}