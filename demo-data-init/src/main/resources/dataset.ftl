<dataset>
    <person name="${.now?datetime?string}"/>
    <person name="${dateUtils.addDays(.now, 3)?datetime?string}"/>
    <person name="petia"/>

    <title label="label1" type="type1" format="format1" person_id="1" date="${dateUtils.addDays(.now, -3)?datetime}"/>
</dataset>
