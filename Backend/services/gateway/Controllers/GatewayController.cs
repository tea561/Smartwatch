using Microsoft.AspNetCore.Mvc;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Google.Apis.YouTube.v3;
using Google.Apis.Services;
using gateway.Models;
using Microsoft.Extensions.Configuration;
using MQTTnet;
using MQTTnet.Client;


namespace gateway.Controllers
{
    [ApiController]
    [Route("/api/[controller]")]

    public class GatewayController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public GatewayController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        /// <summary>
        /// Gets user's health parameters.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Health parameters.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetAllParameters/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetAllParameters(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getAllParameters?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<HealthParameters>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets values of one parameter for today.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Value of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetParameterForDay/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetParameterForDay(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getParameterForDay?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets values of one parameter for week.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Values of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetParameterForWeek/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetParameterForWeek(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getParameterForWeek?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets sum of all values for today.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Values of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetDailySum/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetDailySum(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getDailySum?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }
        /// <summary>
        /// Gets values for one week.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <param name="param">Name of parameter</param>
        /// <returns>Values of parameter.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetDailySumForWeek/{param}/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetDailySumForWeek(string param, int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getDailySumForWeek?userID={userID}&param={param}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }
        /// <summary>
        /// Gets user's friends.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>List of friends.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetFriends/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetFriends(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/getFriends/{userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<List<User>>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }


        /// <summary>
        /// Gets specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>User.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetUser/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetUser(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/{userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<User>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets parameters for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Vitals.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetVitals/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetVitals(int userID)
        {

            Parameters parameters = new Parameters();

            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getVitals?userID={userID}"))
                {
                    
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var resp = await response.Content.ReadFromJsonAsync<Parameters>();
                        if(resp != null){
                            parameters = resp;
                        }
                        else {
                            return StatusCode(500);
                        }

                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
                        
            }
            SleepData sleepData = new SleepData();
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getSleepHoursForDay?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var sleepHours = await response.Content.ReadFromJsonAsync<List<SleepData>>();
                        if(sleepHours != null){
                            sleepData = sleepHours[0];
                        }
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }

            var obj = new 
            {
                sys = parameters.Sys,
                dias = parameters.Dias, 
                pulse = parameters.Pulse,
                sleepHours = sleepData.SleepHours,
                timestampVitals = parameters.Timestamp, 
                timestampSleepData = sleepData.Timestamp,
                userID = parameters.UserID
            };
            return Ok(obj);
        }

        /// <summary>
        /// Gets parameters for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Vitals.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetParameters/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetParameters(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getVitals?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<Parameters>();
                        return Ok(parameters);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets pulse for last 24h.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Pulse values for last 24h.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetPulseForDay/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetPulseForDay(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getPulseForDay?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var pulseArr = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(pulseArr);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets pulse for last 7 days.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Pulse values for last 7 days.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetPulseForWeek/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetPulseForWeek(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getPulseForWeek?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var pulseArr = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(pulseArr);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets sys pressure for last 24h.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Sys pressure values for last 24h.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetSysPressureForDay/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSysPressureForDay(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getSysPressureForDay?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var sysPressureArr = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(sysPressureArr);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets sys pressure for last 7 days.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Sys pressure values for last 7 days.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetSysPressureForWeek/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSysPressureForWeek(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getSysPressureForWeek?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var sysPressureArr = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(sysPressureArr);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets dias pressure for last 24h.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Dias pressure values for last 24h.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetDiasPressureForDay/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetDiasPressureForDay(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getDiasPressureForDay?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var diasPressureArr = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(diasPressureArr);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets dias pressure for last 7 days.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Dias pressure values for last 7 days.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetDiasPressureForWeek/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetDiasPressureForWeek(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getDiasPressureForWeek?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var diasPressureArr = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(diasPressureArr);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets sleep hours for last 7 days.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Sleep hours for last 7 days.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetSleepHoursForWeek/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetSleepHoursForWeek(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getSleepHoursForWeek?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var sleepHours = await response.Content.ReadFromJsonAsync<List<SleepData>>();
                        return Ok(sleepHours);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets calories for last 7 days.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Burned calories for last 7 days.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetCaloriesForWeek/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetCaloriesForWeek(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getCaloriesForWeek?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var calories = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(calories);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets steps for last 7 days.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Steps for last 7 days.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetStepsForWeek/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetStepsForWeek(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getStepsForWeek?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var sleepHours = await response.Content.ReadFromJsonAsync<List<OneParameter>>();
                        return Ok(sleepHours);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Gets vitals and recommended YouTube resource for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>Vitals and recommended YouTube resource.</returns>
        /// <response code="404">User not found.</response>
        [HttpGet("GetStatus/{userID}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> GetStatus(int userID)
        {

            ResultData resultData = new ResultData();
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://data:3333/getVitals?userID={userID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var parameters = await response.Content.ReadFromJsonAsync<Parameters>();
                        resultData.HealthParameters = parameters;
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }

            string searchTerm = "neutral";

            if (resultData.HealthParameters?.Sys > 130 || resultData.HealthParameters?.Dias > 90)
            {
                if (resultData.HealthParameters?.Pulse > 80)
                    searchTerm = "calm";
                else
                    searchTerm = "classical";
            }
            else if (resultData.HealthParameters?.Pulse > 80)
            {
                searchTerm = "relaxing";
            }
            else if (resultData.HealthParameters?.Pulse < 60 || resultData.HealthParameters?.Sys < 120 || resultData.HealthParameters?.Dias < 70)
            {
                searchTerm = "upbeat";
            }

            var youtubeService = new YouTubeService(new BaseClientService.Initializer()
            {
                ApiKey = _configuration["ApiKey"],
                ApplicationName = this.GetType().ToString()
            });



            var searchListRequest = youtubeService.Search.List("snippet");
            searchListRequest.Q = QueryGenerator.QueryGenerator.getQueryParameter(searchTerm);
            searchListRequest.MaxResults = 1;
            searchListRequest.Type = "video,playlist";

            var searchListResponse = await searchListRequest.ExecuteAsync();

            resultData.ResourceTitle = searchListResponse.Items[0].Snippet.Title;
            string resourceId;

            resourceId = (searchListResponse.Items[0].Id.Kind == "youtube#playlist") ? searchListResponse.Items[0].Id.PlaylistId : searchListResponse.Items[0].Id.VideoId;
            resultData.ResourceUrl = $"https://www.youtube.com/watch?v={resourceId}";

            return Ok(resultData);

        }

        /// <summary>
        /// Post vital parameters for specific user.
        /// </summary>
        /// <param name="parameters">Vital parameters</param>
        /// <returns>True if vitals are successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        ///             "sys": 120,
        ///             "dias": 80,
        ///             "pulse": 90,
        ///             "timestamp": 1652119779,
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Vitals successfully added.</response>

        [HttpPost("PostVitals")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> Post([FromBody] Parameters parameters)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(parameters);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PostAsync("http://data:3333/postVitals", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        await this.PublishVitals(serializedObject);
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Post sleep hours for specific user.
        /// </summary>
        /// <param name="parameters">Sleep hours and date</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        ///             "sleepHours": "7:30:20",
        ///             "timestamp": 1652119779,
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostSleepHours")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostSleepHours([FromBody] SleepData sleepHours)
        {
            using (var httpClient = new HttpClient())
            {
                Console.WriteLine("OK");
                var serializedObject = JsonConvert.SerializeObject(sleepHours);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PostAsync("http://data:3333/postSleepHours", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        //await this.PublishVitals(serializedObject);
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Post sleep hours for specific user.
        /// </summary>
        /// <param name="steps">Steps and date</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        ///             "steps": 1233,
        ///             "time": "22 Jan 2017 12:34",
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostSteps")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostSteps([FromBody] OneParameter steps)
        {
            using (var httpClient = new HttpClient())
            {
                Console.WriteLine("OK");
                var serializedObject = JsonConvert.SerializeObject(steps);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PostAsync("http://data:3333/postSteps", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        //await this.PublishVitals(serializedObject);
                        var obj = new
                        {
                            result = apiResponse
                        };
                        return Ok(obj);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Post user.
        /// </summary>
        /// <param name="user">User data</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         POST /Gateway
        ///         {
        /// {
        ///     "_id": 2,
        ///     "username": "user23",
        ///     "age": 27,
        ///     "gender": "m",
        ///     "height": 193,
        ///     "weight": 90
        /// }
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>

        [HttpPost("PostUser")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> PostUser([FromBody] User user)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(user);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
            
                using (var response = await httpClient.PostAsync("http://users:6080/user", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<int>();
                        //await this.PublishVitals(serializedObject);
                        var obj = new
                        {
                            result = apiResponse
                        };
                        return Ok(obj);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                return BadRequest();
            }
        }


        /// <summary>
        /// Calculate burned calories and save in db.
        /// </summary>
        /// <param name="activityParameters">Activity parameters</param>
        /// <returns>True if data is successfully added. Otherwise, false.</returns>
        /// <remarks>
        ///     Sample request:
        ///         Parameters
        ///         POST /Gateway
        ///         {
        /// {
        ///     "_id": 2,
        ///     "Duration": 23.3,
        ///     "Heart_Rate": 110.0,
        ///     "Body_Temp": 40.0 ,
        /// }
        ///         }
        ///
        ///</remarks>
        /// <response code="400">Post parameters not defined.</response>
        /// <response code="200">Data successfully added.</response>
        [HttpPost("CalculateCalories")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<IActionResult> CalculateCalories([FromBody] ActivityParameters activityParameters)
        {
            User? user = new User();
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.GetAsync($"http://users:6080/user/{activityParameters.UserID}"))
                {

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        user = await response.Content.ReadFromJsonAsync<User>();    

                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }

                if (user == null)
                    return BadRequest();

                var obj = new
                {
                    Gender = user.Gender.Equals("m") ? 0 : 1,
                    Age = user.Age,
                    Height = user.Height,
                    Weight = user.Weight,
                    Duration = activityParameters.Duration,
                    Heart_Rate = activityParameters.Heart_Rate,
                    Body_Temp = activityParameters.Body_Temp
                };
                Console.WriteLine(activityParameters);
                Console.WriteLine(obj);

                var serializedObject = JsonConvert.SerializeObject(obj);
                var content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
            

                float calories = -1.0f;
                using (var response = await httpClient.PostAsync("http://calories:7080/predict", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        calories = await response.Content.ReadFromJsonAsync<float>();
                        Console.WriteLine(calories);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }

                }

                if(calories != -1.0f)
                {
                    var postData = new {
                        calories = calories,
                        userID = activityParameters.UserID
                    };

                    var serializedData = JsonConvert.SerializeObject(postData);
                    var postContent = new StringContent(serializedData, Encoding.UTF8, "application/json");

                    using (var response = await httpClient.PostAsync("http://data:3333/postCalories", postContent))
                    {
                        Console.WriteLine("DATA POST");
                        if (response.StatusCode == System.Net.HttpStatusCode.OK)
                        {
                            var resp = await response.Content.ReadFromJsonAsync<bool>();
                            return Ok(resp);
                        }
                        else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                        {
                            var errorResponse = await response.Content.ReadAsStringAsync();
                            return NotFound(errorResponse);
                        }
                        else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                        {
                            return StatusCode(500);
                        }

                    }
                    
                }

                return BadRequest();


            }
        }


        /// <summary>
        /// Delete vital parameters for specific user.
        /// </summary>
        /// <param name="userID">User ID</param>
        /// <returns>True if vitals are successfully added. Otherwise, false.</returns>
        /// <response code="200">Vitals successfully deleted.</response>
        [HttpDelete("DeleteDataForUser/{userID}")]
        public async Task<ActionResult> DeleteDataForUser(int userID)
        {
            using (var httpClient = new HttpClient())
            {
                using (var response = await httpClient.DeleteAsync($"http://data:3333/deleteVitals?userID={userID}"))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        return Ok();
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                    else
                    {
                        return BadRequest();
                    }
                }
            }
        }

        /// <summary>
        /// Update vital parameters for specific user.
        /// </summary>
        /// <param name="parameters">Vital parameters</param>
        /// <returns>True if vitals are successfully updated. Otherwise, false.</returns>
        /// <response code="400">Put parameters not defined.</response>
        /// <response code="200">Vitals successfully added.</response>
        /// <remarks>
        ///     Sample request:
        ///         
        ///         PUT /Gateway
        ///         {
        ///             "sys": 120,
        ///             "dias": 80,
        ///             "pulse": 90,
        ///             "timestamp": 1652119779,
        ///             "userID": 3
        ///         }
        ///
        ///</remarks>
        [HttpPut("UpdateVitals")]
        public async Task<ActionResult> Put([FromBody] Parameters parameters)
        {
            using (var httpClient = new HttpClient())
            {
                var serializedObject = JsonConvert.SerializeObject(parameters);
                StringContent content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PutAsync("http://data:3333/updateVitals", content))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<bool>();
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                }

                return BadRequest();
            }
        }

        /// <summary>
        /// Add friend.
        /// </summary>
        /// <param name="userID">First user</param>
        /// <param name="friendID">Second user</param>

        /// <returns>True if friend relationship is saved. Otherwise, false.</returns>
        /// <response code="400">Put parameters not defined.</response>
        /// <response code="200">Friend relationship added.</response>
        [HttpPut("AddFriend/{userID}/{friendID}")]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<ActionResult> AddFriend(int userID, int friendID)
        {
            using (var httpClient = new HttpClient())
            {
                //var serializedObject = JsonConvert.SerializeObject(parameters);
                //StringContent content = new StringContent(serializedObject, Encoding.UTF8, "application/json");
                using (var response = await httpClient.PutAsync($"http://users:6080/user/addFriend/{userID}/{friendID}", null))
                {
                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var apiResponse = await response.Content.ReadFromJsonAsync<User>();
                        return Ok(apiResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                    {
                        var errorResponse = await response.Content.ReadAsStringAsync();
                        return NotFound(errorResponse);
                    }
                    else if (response.StatusCode == System.Net.HttpStatusCode.InternalServerError)
                    {
                        return StatusCode(500);
                    }
                }

                return BadRequest();
            }
        }

        private async Task PublishVitals(string payload)
        {
            var mqttFactory = new MqttFactory();

            using (var mqttClient = mqttFactory.CreateMqttClient())
            {
                var mqttClientOptions = new MqttClientOptionsBuilder()
                    .WithTcpServer("mqtt", 1883)
                    .WithClientId("gateway")
                    .Build();

                mqttClient.ConnectedAsync += conn =>
                {
                    Console.WriteLine("The MQTT client is connected.");
                    return Task.CompletedTask;
                };
                mqttClient.DisconnectedAsync += disc =>
                {
                    Console.WriteLine("The MQTT client is disconnected.");
                    return Task.CompletedTask;
                };

                var response = await mqttClient.ConnectAsync(mqttClientOptions, CancellationToken.None);
                Console.WriteLine(response.ResultCode);

                var applicationMessage = new MqttApplicationMessageBuilder()
                    .WithTopic("projekat/vitals")
                    .WithPayload(payload)
                    .Build();
                await mqttClient.PublishAsync(applicationMessage, CancellationToken.None);
                Console.WriteLine("MQTT application message is published.");

                var disconnectOptions = new MqttClientDisconnectOptionsBuilder()
                    .WithReason(MqttClientDisconnectReason.NormalDisconnection)
                    .Build();
                await mqttClient.DisconnectAsync(disconnectOptions);

            }
        }
    }
}