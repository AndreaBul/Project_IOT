#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "contiki.h"
#include "coap-engine.h"
#include "coap-blocking-api.h"
#include "sys/etimer.h"
#include "dev/leds.h"
#include "os/dev/serial-line.h"

/* Log configuration */
#include "sys/log.h"
#define LOG_MODULE "App"
/*	Declare server IP	*/
#define SERVER_EP "coap://[fd00::1]:5683" 
#define REQ_INTERVAL 5

/*	Declare external resources to be activated	*/
extern coap_resource_t co_sensor;


static struct etimer e_timer;	//Timer
char *reg_service_url = "/registration";	//Resource URL for registration
static bool registration_status = false;



/* Declare and auto-start this file's process */
PROCESS(device_process, "Sensor/Actuator Device");
AUTOSTART_PROCESSES(&device_process);


/*------------------------------------------------------------------------------------------*/

/*	Handler for the response from theserver	*/

void client_chunk_handler(coap_message_t *response) {
 
	if(response == NULL) { 
		printf("Request timed out\n"); 
		return;
	}
	const uint8_t *chunk;
	coap_get_payload(response, &chunk);
	printf("Received Response: %s\n", (char *)chunk);

	//If the response is "Accept" I registered the device
	if(strcmp( (char *)chunk, "Accept") == 0){
		printf("Request was accepted.");
		registration_status = true;
	} else {
		printf("Request wasn't accepted.");
		registration_status = false;
	}
}

/*------------------------------------------------------------------------------------------*/

PROCESS_THREAD(device_process, ev, data){

	static coap_endpoint_t server_ep;
  	static coap_message_t request[1];  /* the packet can be treated as pointer */

	PROCESS_BEGIN();

	//activate resource
	coap_activate_resource(&co_sensor, "co_sensor");

	printf("Type \"register\" in the console to register the device on the cloud application.\n"); 

	while(1){

		//Possible events: timer, unregister event, serial line message. 
		PROCESS_WAIT_EVENT_UNTIL(ev == PROCESS_EVENT_TIMER || ev == serial_line_event_message);

		if(registration_status){
			//trigger co_sensor. 
			co_sensor.trigger();
			etimer_reset(&e_timer);
		}
		//Only if !registered I accept command line values
		else if(ev == serial_line_event_message && !registration_status){
			//I register the device
			if(strcmp(data, "register") == 0){
				/*	Node Registration	*/

				// Populate the coap_endpoint_t data structure
				coap_endpoint_parse(SERVER_EP, strlen(SERVER_EP), &server_ep);
				// Prepare the message
				coap_init_message(request, COAP_TYPE_CON, COAP_POST, 0);
				coap_set_header_uri_path(request, reg_service_url);

				while(!registration_status){
					printf("Sending registration request to the server\n");
					//Send the registration request to the server
					COAP_BLOCKING_REQUEST(&server_ep, request, client_chunk_handler);
				}

				printf("Registration completed with status: %s\n", registration_status ? "true" : "false");		
				etimer_set(&e_timer, CLOCK_SECOND * REQ_INTERVAL);
				

			}
		}
	}
  	PROCESS_END();
}
