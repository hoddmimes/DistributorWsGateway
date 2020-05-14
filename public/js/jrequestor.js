
function defaultErrorHandler( jqXHR, status, err )
{
    alert( this.url + " failed, reason: " + jqXHR.responseText );
}

class jrequestor
{

    constructor()
    {
    }

    get( pUrl, pSuccessCallback, pErrorCallback )
    {
        var ajaxParams = {  url : pUrl, type : 'GET'};

        var posting = $.ajax( ajaxParams );

        posting.done( pSuccessCallback );

        if (pErrorCallback == null) {
            posting.fail( defaultErrorHandler );
        } else {
            posting.fail( pErrorCallback );
        }
    }

    delete( pUrl, pSuccessCallback, pErrorCallback )
    {
       var ajaxParams = {  url : pUrl, type : 'DELETE'};

       var posting = $.ajax( ajaxParams );

       posting.done( pSuccessCallback );

       if (pErrorCallback == null) {
          posting.fail( defaultErrorHandler );
       } else {
           posting.fail( pErrorCallback );
       }
    }

    put( pUrl, pRqstMsg, pSuccessCallback, pErrorCallback )
    {
            var ajaxParams = {  url : pUrl, type : 'PUT', contentType : 'application/json; charset=iso-8859-1'};

            if (pRqstMsg != null) {
                if (typeof pRqstMsg === 'string') {
                    ajaxParams['data'] = pRqstMsg;
                } else {
                      ajaxParams['data'] = JSON.stringify(pRqstMsg);
                }
            }

            var posting = $.ajax( ajaxParams );
            posting.done( pSuccessCallback );

            if (pErrorCallback == null) {
                posting.fail( defaultErrorHandler );
            } else {
                posting.fail( pErrorCallback );
            }
    }

    post( pUrl, pRqstMsg, pSuccessCallback, pErrorCallback )
    {
        var ajaxParams = {  url : pUrl, type : 'POST', contentType : 'application/json; charset=iso-8859-1'};

        if (pRqstMsg != null) {
            if (typeof pRqstMsg === 'string') {
                ajaxParams['data'] = pRqstMsg;
            } else {
                  ajaxParams['data'] = JSON.stringify(pRqstMsg);
            }
        }

        var posting = $.ajax( ajaxParams );
        posting.done( pSuccessCallback );

        if (pErrorCallback == null) {
            posting.fail( defaultErrorHandler );
        } else {
            posting.fail( pErrorCallback );
        }
    }
}