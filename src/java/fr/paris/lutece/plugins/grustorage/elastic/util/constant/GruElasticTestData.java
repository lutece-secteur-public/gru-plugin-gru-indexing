/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.grustorage.elastic.util.constant;

public class GruElasticTestData
{
    public static final String JSON = "{\n" + "   \"notification\": {\n" + "    \"user_guid\": 1108,\n" +
        " \"notification_source\": \"Gestion des sollicitations\",\n" + "    \"email\": \"mdupont@domain.com\",\n" +
        "    \"crm_status_id\": 1,\n" + "    \"notification_type\": \"\",\n" + "    \"demand_id\": 1099,\n" +
        "    \"demand_id_type\": 14,\n" + "    \"demand_max_step\": 5,\n" + "    \"demand_user_current_step\": 3,\n" +
        "    \"demand_state\": 9,\n" + "\n" + "    \"user_email\": {\n" +
        "      \"sender_name\": \"Mairie de Paris\",\n" + "      \"sender_email\": \"no_reply@paris.fr\",\n" +
        "      \"recipient\": \"mdupont@domain.com\",\n" +
        "      \"subject\": \"[Mairie de Paris] Demande de carte de stationnement\",\n" +
        "      \"message\": \"Bonjour Monsieur John Doe, Votre demande de carte de stationnement est en attente de validation. ...\",\n" +
        "      \"cc\": \"\",\n" + "      \"cci\": \"\"\n" + "    },\n" + "\n" + "    \"user_dashboard\": {\n" +
        "      \"status_text\": \"En attente de validation\",\n" + "      \"sender_name\": \"Mairie de Paris\",\n" +
        "      \"subject\": \"test notif\",\n" + "      \"message\": \"this is a test\",\n" + "      \"data\": \"\"\n" +
        "    },\n" + "\n" + "    \"user_sms\": {\n" + "      \"phone_number\": \"0680125345\",\n" +
        "      \"message\": \"Votre demande de carte de stationnement est en attente de validation.\"\n" + "    },\n" +
        "    \n" + "    \"backoffice_logging\": {\n" + "      \"status_text\": \"En attente de validation\",\n" +
        "      \"message\": \"Traitement de la demande en cours par le service de la DVD\",\n" +
        "      \"notified_on_dashboard\": 1,\n" + "      \"notified_by_email\": 1,\n" +
        "      \"notified_by_sms\": 1,\n" + "      \"display_level_dashboard_notification\": 2,\n" +
        "      \"view_dashboard_notification\": \"\",\n" + "      \"display_level_email_notification\": 2,\n" +
        "      \"view_email_notification\": \"Email envoyé à l'adresse : mdupont@domain.com – Objet : ... _ Message : ...\",\n" +
        "      \"display_level_sms_notification\": 2,\n" +
        "      \"view_sms_notification\": \"SMS envoyé au numéro 0680125345 _ Message : Votre demande de carte de stationnement est en attente de validation.\"\n" +
        "    }\n" + "  }\n" + "}";
    public static final String DEMAND_UPDATE = "{\n" + "\"doc\":{\n" + "\"utilisateur\":\n" + "{\n" +
        "\"user_guid\":1108\n" + "},\n" + "\" demand_id\": \"1100\",\n" + "\"demand_id_type\": 10,\n" +
        "\"demand_max_step\": 4,\n" + "\"demand_user_current_step\": 2,\n" + "\"demande_state\":9,\n" +
        "\"notification_type\": \"blabla\",\n" + "\"date_demande\":\"2015-03-31\",\n" + "\"crm_status_id\":1,\n" +
        "\"reference\": \"PZQu4rocRy60hO2seUEziQ\",\n" + "\"suggest\":\n" + "{\n" + "\"input\":" + "[" +
        "\"PZQu4rocRy60hO2seUEziQ\"" + "],\n" + "\"output\": \"John Doe\",\n" + "\"payload\":\n" + "{\n" +
        "\"user_guid\":\"1108\",\n" + "\"birthday\":\"20/03/1980\",\n" + "\"telephoneNumber\":\"0142556\",\n" +
        "\" email\":\"amoumya@gmail.com\",\n" + "\"reference\":\"PZQu4rocRy60hO2seUEziQ\"\n" + "}\n}\n}\n}";
    public static final String DEMAND = "{\n" + "\"utilisateur\":\n" + "{\n" + "\"user_guid\":1108\n" + "},\n" +
        "\" demand_id\": \"1150\",\n" + "\"demand_id_type\": 15,\n" + "\"demand_max_step\": 4,\n" +
        "\"demand_user_current_step\": 2,\n" + "\"demande_state\":9,\n" + "\"notification_type\": \"blabla\",\n" +
        "\"date_demande\":\"2015-03-31\",\n" + "\"crm_status_id\":1,\n" +
        "\"reference\": \"PZQu4rocRy60hO2seUEziQ\",\n" + "\"suggest\":\n" + "{\n" + "\"input\":" + "[" +
        "\"PZQu4rocRy60hO2seUEziQ\"" + "],\n" + "\"output\": \"John Doe\",\n" + "\"payload\":\n" + "{\n" +
        "\"user_guid\":\"1108\",\n" + "\"birthday\":\"20/03/1980\",\n" + "\"telephoneNumber\":\"0142556\",\n" +
        "\" email\":\"kingdioum@hotmail.fr\",\n" + "\"reference\":\"PZQu4rocRy60hO2seUEziQ\"\n" + "}\n}\n}";
    public static final String USER = "{\n" + "\"user_guid\":\"1120\",\n" + "\"email\": \"ndiambe@somewhere.com\"," +
        "\"name\": \"Ndiabe Darou\",\n" + "\"stayConnected\" : \"true\",\n" + "\"street\" : \" rue test\",\n" +
        "\"telephoneNumber\" : \"0142556\",\n" + "\"city\" : \"Paris\",\n" + "\"cityOfBirth\" : \"london\",\n" +
        "\"birthday\" : \"20/03/1980\",\n" + "\"civility\":\"Mr\",\n" + "\"postalCode\" : \"75019\",\n" +
        "\"suggest\":" + "{\n" + "\"input\":" + "[" + "\"Ndiabe\"," + "\"Darou\"," + "\"0142556\"," +
        "\"ndiambe@somewhere.com\"" + "],\n" + "\"output\": \"Ndiambe Darou\",\n" + "\"payload\":" + "{" +
        "\"user_guid\":\"1120\",\n" + "\"birthday\": \"20/03/1980\",\n" + "\"telephoneNumber\":\"0142556\",\n" +
        "\"email\":\"ndiambe@somewhere.com\"\n" + "}\n" + "}\n" + "}";
    public static final String NOTIFICATION = "{\n" + "\"sollicitation\":" + "{\n" + "\"demand_id\": \"1150\"\n" +
        "},\n" + "\"status_text\": \"En attente de validation\",\n" +
        "\"message\": \"Traitement de la demande en cours par le service de la DVD\",\n" +
        "\"notified_on_dashboard\": 1,\n" + "\"notified_by_email\": 1,\n" + "\"notified_by_sms\": 1,\n" +
        "\"display_level_dashboard_notification\": 2,\n" + "\"view_dashboard_notification\":\" \",\n" +
        "\"display_level_email_notification\": 2,\n" +
        "\"view_email_notification\": \"Email envoyé à l'adresse : john.doe@somewhere.com – Objet : ... _ Message : ...\",\n" +
        "\"display_level_sms_notification\": 2,\n" +
        "\"view_sms_notification\": \"SMS envoyé au numéro 0680125345 _ Message : Votre demande de carte de stationnement est en attente de validation.\",\n" +
        "\"date_sollicitation\":\"2015-03-31\",\n" + "\"user_email\": {\n" + "\"sender_name\": \"Mairie de Paris\",\n" +
        "\"sender_email\":\"no_reply@paris.fr\",\n" + "\"recipient\": \"john.doe@somewhere.com\",\n" +
        "\"subject\": \"[Mairie de Paris] Demande de carte de stationnement\",\n" +
        "\"message\": \"Bonjour Monsieur John Doe, Votre demande de carte de stationnement est en attente de validation. ...\"\n" +
        "},\n" + "\"user_dashboard\":{\n" + "\"status_text\":\"En attente de validation\",\n" +
        "\"sender_name\": \"Mairie de Paris\",\n" + "\"subject\": \"test notif\",\n" +
        "\"message\": \"this is a test\",\n" + "\"data\": \" \"\n" + "},\n" + "\"user_sms\": {\n" +
        "\"phone_number\": \"0680125345\",\n" +
        "\"message\": \"Votre demande de carte de stationnement est en attente de validation.\"" + "}\n" + "}";
}
