using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace gateway.Models
{
    public class OneParameter
    {
        [JsonProperty("value")]
        public double? Value { get; set; }

        [JsonProperty("time")]
        public string? Time {get; set; }

        [JsonProperty("userID")]
        public int? UserID { get; set; }

    }

    
}