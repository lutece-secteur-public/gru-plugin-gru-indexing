![](http://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=gru-plugin-gru-indexing-deploy)
# Module gru indexing

## Introduction

This plugin enables to index objects `fr.paris.lutece.plugins.grubusiness.business.customer.Customer` of the library `gru-library-grubusiness` . A search on customers can be then performed.

This plugin contains two indexing implementations:
 
* indexing with Lucene: the search can only be done on the first name and/or the last name of the customers. It is the default implementation.
* indexing with Elasticsearch: the search can be done on every indexed fields.


## Using Lucene

 **Indexing** 

The class managing the indexing is `fr.paris.lutece.plugins.gruindexing.business.lucene.LuceneCustomerDAO` . It is also configured as a bean in the Spring context.It can be configured:
 
* The first constructor argument gives the path of the index folder. By default, the index folder is in the webapp.
* The second constructor argument indicates whether the index folder is in the webapp (value = `true` ) or not (value = `false` ). By default, the value is `true` .


 **Search** 

The class managing the search is `fr.paris.lutece.plugins.gruindexing.business.lucene.LuceneCustomerDAO` . See the above section **Indexing** to configure the class.

Only the first name and/or the last name can be searched.

 **Autocomplete search** 

The class managing the autocomplete search is `fr.paris.lutece.plugins.gruindexing.web.rs.lucene.LuceneAutoCompleteRestService` . It is also configured as a bean in the Spring context.It is a web service which can be called at the URL `/rest/lucene/autocomplete` with a GET method. The parameter `query` must contain the term(s) to search.

Only the first name and/or the last name can be searched.

The autocomplete search returns a JSON string with the following fields:
 
*  `output` : the text displayed by the autocomplete search. It is always the first name + the last name of the customer.
*  `search` : the list of fields used to execute a search. Why this field is present: the autocomplete search removes the duplicate results. Thus, when a result is selected, a second search can be needed to display the duplicate results. The second search is based on this list of fields. It always contains the field `first_name` and the field `last_name` .
Here is an example:
```

{
  "autocomplete": [{
    "output": "John Doe",
    "search":{
      "first_name":"John",
      "last_name":"Doe"
    }
  }, {
    "output": "John Black",
    "search":{
      "first_name":"John",
      "last_name":"Black"
    }
  }]
}
                
```


## Using Elasticsearch

Before using Elasticsearch, please modify the following keys in the properties file:
 
*  `gru-indexing.urlElk` : the URL of the Elasticsearch server
*  `gru-indexing.index` : the name of the Elasticsearch index
*  `gru-indexing.typeUser` : the Elasticsearch index type for customers


 **Indexing** 

The class managing the indexing is `fr.paris.lutece.plugins.gruindexing.business.elasticsearch.ElasticSearchCustomerDAO` . It is also configured as a bean in the Spring context.

The indexing query is defined in the file `WEB-INF/plugins/gruindexing/elasticsearch_customer_indexing.template` . It contains placeholders (like `${user_cid}` ) which are replaced with actual values at index-time. Some placeholders are pre-defined:
 
*  `${customer_id}` : the customer id
*  `${connection_id}` : the connection id of the customer
*  `${email}` : the email of the customer
*  `${last_name}` : the last name of the customer
*  `${family_name}` : the family name of the customer. It corresponds to the name a person has before getting married.
*  `${first_name}` : the first name of the customer
*  `${mobile_phone_number}` : the mobile phone number of the customer
*  `${fixed_phone_number}` : the fixed phone number of the customer
*  `${birthday}` : the birthday of the customer
*  `${civility}` : the civility of the customer
Other placeholders are retrieved from extra attributes of the object `fr.paris.lutece.plugins.grubusiness.business.customer.Customer` .

In the same way, some indexed fields are pre-defined:
 
*  `customer_id` : the customer id
*  `connection_id` : the connection id of the customer
*  `email` : the email of the customer
*  `last_name` : the last name of the customer
*  `family_name` : the family name of the customer. It corresponds to the name a person has before getting married.
*  `first_name` : the first name of the customer
*  `mobile_phone_number` : the mobile phone number of the customer
*  `fixed_phone_number` : the fixed phone number of the customer
*  `birthday` : the birthday of the customer
*  `civility` : the civility of the customer
Other indexed fields can be added as desired.

In order to use autocomplete search, the template must contain a field `suggest` and a field `payload` . The field `suggest` is an Elasticsearch standard completion field. The field `payload` must contain the following fields:
 
*  `output` : the text displayed by the autocomplete search
*  `search` : the list of fields used to execute a search. Why this field is present: the autocomplete search removes the duplicate results. Thus, when a result is selected, a second search can be needed to display the duplicate results. The second search is based on this list of fields.
Before the version 5.0 of Elasticsearch, the field `payload` must be contained in the field `suggest` . Here is an example:
```

"suggest":{
  "input":[
    "${first_name} ${last_name}",
    "${last_name} ${first_name}",
    "${telephoneNumber}",
    "${fixed_telephone_number}"
  ],
  "payload":{
    "output":"${first_name} ${last_name}",
    "search":{
      "first_name":"${first_name}",
      "last_name":"${last_name}"
    }
  }
}
                
```
After the version 5.0 of Elasticsearch, the field `payload` must be a field of the document, as other fields. Here is an example:
```

"suggest":{
  "input":[
    "${first_name} ${last_name}",
    "${last_name} ${first_name}",
    "${telephoneNumber}",
    "${fixed_telephone_number}"
  ]
},
"payload":{
  "output":"${first_name} ${last_name}",
  "search":{
    "first_name":"${first_name}",
    "last_name":"${last_name}"
  }
}
                
```


 **Search** 

The class managing the search is `fr.paris.lutece.plugins.gruindexing.business.elasticsearch.ElasticSearchCustomerDAO` . It is also configured as a bean in the Spring context.

The search can be configured by modifying the following keys in the properties file:
 
*  `gru-indexing.sizeSearchParamValue` : the number of results returned by the search


 **Autocomplete search** 

The class managing the autocomplete search is `fr.paris.lutece.plugins.gruindexing.web.rs.elasticsearch.ElasticSearchAutoCompleteRestService` . It is also configured as a bean in the Spring context.It is a web service which can be called at the URL `/rest/elasticsearch/autocomplete` with a GET method. The parameter `query` must contain the term(s) to search.

The autocomplete search query is defined in the file `WEB-INF/plugins/gruindexing/elasticsearch_autocomplete.template` . It contains a placeholder `${query}` which is replaced with the actual search query at search-time.

The autocomplete search returns a JSON string with the following fields:
 
*  `output` : the field `output` defined in the indexing template file
*  `search` : the field `search` defined in the indexing template file
Here is an example:
```

{
  "autocomplete": [{
    "output": "John Doe",
    "search":{
      "first_name":"John",
      "last_name":"Doe"
    }
  }, {
    "output": "John Black",
    "search":{
      "first_name":"John",
      "last_name":"Black"
    }
  }]
}
                
```



[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/plugin-gru-indexing/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*