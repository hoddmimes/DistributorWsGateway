


function dblfmt( value, format) {
    var arr = format.split(":");
    var decimals = parseInt(arr[1],10);
    return Number( value ).toFixed( decimals );
}

function formatTime( deltatime ) {
    hh = Math.floor(deltatime / 3600);
    mm = Math.floor((deltatime - (hh * 3600)) / 60);
    sec = deltatime - (hh * 3600) - (mm * 60);
    if (hh == 0) {
        return ("00" + mm).slice(-2) + ":" + ("00" + sec).slice(-2);
    } else {
        return ("00" + hh).slice(-2)  + ":" + ("00" + mm).slice(-2) + ":" + ("00" + sec).slice(-2);
    }
}

function isDeltaTime( format ) {
    return format.startsWith("deltatime");
}

function isDouble( format ) {
    return format.startsWith("double");
}

function formatValue( value, format )
{
   if (isDeltaTime( format )) {
     return formatTime( value );
   }

   if (isDouble( format )) {
    return dblfmt( value, format );
   }

   return value;
}

function tabGetJKey( pTabDef, jKey )
{
    var tKeys = pTabDef.keys;
    for( var i = 0; i <  tKeys.length; i++ ) {
      var k = tKeys[i];
      if (k.jkey == jKey) {
            return k;
      }
    }
    return null;
}

function purgeTable( pTable, pMaxRows, pTopDown )
{
    while (pTable.rows.length > (pMaxRows + 1)) {
        if (pTopDown == 'true') {
            pTable.deleteRow( pTable.rows.length - 1);
        } else {
            pTable.deleteRow( 1 );
        }
    }
}

function tabFindRow( pTable, pTabKey, jKeyValue ) {
        for( var i = 1; i < pTable.rows.length; i++) {
          if (pTable.rows[i].cells[ pTabKey.column].innerHTML == jKeyValue) {
            return pTable.rows[i];
          }
        }
        return null;
}


class jtable
{
    constructor( pTableDef )
    {
        this.tableDef = pTableDef;
        this.table = document.getElementById( pTableDef.tableName );
    }


    clear() {
        var tSize = this.table.rows.length;
        for (var i = tSize - 1; i > 0; i--) {
            this.table.deleteRow(i);
        }
    }


    deleteRow( pRowNumber )
    {

        if ( this.table.rows.length <= 1)
        {
          return;
        }

        if (arguments.length > 0) {
            if (pRowNumber < 1) {
                return;
            }
            this.table.deleteRow( pRowNumber );
            return;
        }

        if (this.tableDef.topDown == 'true')
        {
            this.table.deleteRow( this.table.rows.length - 1 );
        } else {
            this.table.deleteRow( 1 );
        }
    }

    valueAt( pRow, pCol  )
    {
        if (pRow < 1 || pRow > this.table.rows.length) {
            return null;
        }
        if (pCol < 0 || pCol > this.tableDef.keys.length) {
          return null;
        }

        return this.table.rows[pRow].cells[pCol].innerHTML;
    }

    deleteRowByKey( jKey, jKeyValue ) {
        var tTabKey = tabGetJKey( this.tableDef, jKey );
        if (tTabKey == null) {
            return Boolean( false );
        }

        for( var i = 1; i < this.table.rows.length; i++) {
            if (this.table.rows[i].cells[ tTabKey.column].innerHTML == jKeyValue) {
                this.table.deleteRow(i);
                return Boolean( true );
            }
        }

         return Boolean( false );
    }

    update( jKey, jKeyValue, jObject)
    {
      var tTabKey = tabGetJKey( this.tableDef, jKey );
      if (tTabKey == null) {
        return Boolean( false );
      }

      var tRow = tabFindRow( this.table, tTabKey, jKeyValue );
      if (tRow == null) {
        return Boolean( false );
      }

      var tKeys = this.tableDef.keys;
      for( var i = 0; i <  tKeys.length; i++ ) {
          var k = tKeys[i];
          var v = jObject[k.jkey];
          if (v != null) {
               tRow.cells[ k.column ].innerHTML = v;
          }
      }
      return Boolean( true );
    }

    insert( jObject )
    {
       var tRow = undefined;
       var tKeys = this.tableDef.keys;

        if (this.tableDef.topDown == 'true') {
           tRow = this.table.insertRow(1);
        } else {
           tRow = this.table.insertRow( this.table.rows.length);
        }

       for( var i = 0; i <  tKeys.length; i++ ) {
          tRow.insertCell(i);
          if (tKeys[i].cssClass != null) {
            tRow.cells[ i ].innerHTML = "";
            tRow.cells[ i ].className = tKeys[i].cssClass;
          }
        }

        if (this.tableDef.onClick != null)
        {
          var fn = window[this.tableDef.onClick];
          tRow.onmouseup = function() {fn.apply(null, [tRow]);}
        }

       for( var i = 0; i <  tKeys.length; i++ ) {
           var k = tKeys[i];
           var v = jObject[k.jkey];
           if (v != null) {
             tRow.cells[ k.column ].innerHTML = formatValue( v, k.type );
           }
           if (k.onClick != null) {
              var fn = window[k.onClick];
              row.cells[ k.column ].onmouseup =  function() {fn.apply(null, [[k.jkey,tRow ]]);}
           }
       }

       if (typeof this.tableDef.maxRows !== "undefined") {
         if (this.tableDef.maxRows > 0) {
            purgeTable( this.table, this.tableDef.maxRows, this.tableDef.topDown );
         }
       }
    }
}