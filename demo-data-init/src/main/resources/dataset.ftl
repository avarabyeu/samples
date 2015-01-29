<dataset>
    <person name="${.now?datetime?string}"/>
    <person name="${dateUtils.addDays(.now, 3)?datetime?string}"/>
    <person name="petia"/>

    <!-- Default JDK's numiric random -->
    <person name="petia-${random.nextInt(100)}"/>

    <!-- Apache Commons String Random -->
    <person name="petia-${stringRandom.randomAlphabetic(10x)}"/>

    <title label="label1" type="type1" format="format1" person_id="1" date="${dateUtils.addDays(.now, -3)?datetime}"/>
</dataset>
