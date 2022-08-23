using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace gateway.Models
{
    public class SleepData
    {
        [JsonProperty("sleepHours")]
        public double? SleepHours { get; set; }

        [JsonProperty("timestamp")]
        public string? Timestamp {get; set; }

        [JsonProperty("userID")]
        public int? UserID { get; set; }

    }

    
}