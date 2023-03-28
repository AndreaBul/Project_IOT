#include "contiki.h"
#include "coap-engine.h"
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "CO_SENSOR"
#define LOG_LEVEL LOG_LEVEL_APP

int PROBABILITY_PRESENCE = 50;
/* Range of values for humidity */
int co_sensor_value = 0;
static unsigned int get_accept = APPLICATION_JSON;

static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset);
static void res_event_handler(void);

/*----------------------------------------------------------------------------------------------*/

EVENT_RESOURCE(co_sensor,
 	   "title=\"Sensor used to detect CO\";methods=\"GET\";rt=\"sensor\";obs\n"
	res_get_handler,
        NULL,
        NULL,
         NULL, 
	res_event_handler);
        
/*----------------------------------------------------------------------------------------------*/

static void res_event_handler(void) {
 
    //Randomly generated co_sensor percentage value for each observation
    co_sensor_value = (rand()%30);
    coap_notify_observers(&co_sensor);
}



static void res_get_handler(coap_message_t *request, coap_message_t *response, uint8_t *buffer, uint16_t preferred_size, int32_t *offset){

	char response_message[COAP_MAX_CHUNK_SIZE];

	coap_get_header_accept(request, &get_accept);
	//Handle only JSON format
	if(get_accept == APPLICATION_JSON){
		
		coap_set_header_content_format(response, APPLICATION_JSON);
		
		int len = snprintf(response_message, COAP_MAX_CHUNK_SIZE, "{\"co_value\":%d}", co_sensor_value);
		if(len > 0){
			//Prepare the message
			memcpy(buffer, (uint8_t*)response_message, len);
			coap_set_header_etag(response, (uint8_t *)&len, 1);
            		coap_set_payload(response, buffer, len);
		} else {
			LOG_INFO("Error: Response message not formed\n");
		}
	} else{
		coap_set_status_code(response, NOT_ACCEPTABLE_4_06);
	   	const char *msg = "Supported content-types:application/json and application/xml";
	    	coap_set_payload(response, msg, strlen(msg));
	}
	
	
}


