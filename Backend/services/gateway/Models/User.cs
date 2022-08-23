using Newtonsoft.Json;

namespace gateway.Models
{
    public class User
    {
        [JsonProperty("_id")]
        public int? _Id {get; set; }
        
        [JsonProperty("username")]
        public string? Username {get; set; }

        [JsonProperty("age")]
        public int? Age { get; set; }

        [JsonProperty("gender")]
        public string? Gender {get; set; }

        [JsonProperty("height")]
        public float? Height {get;set;}

        [JsonProperty("weight")]
        public float? Weight { get; set; }

        [JsonProperty("friends")]
        public int[]? Friends {get; set;}

    }

    
}