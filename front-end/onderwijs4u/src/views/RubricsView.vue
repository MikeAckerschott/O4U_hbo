<template>
  <div class="container-fluid mt-5">
    <div class="row mb-3">
      <div class="col-md-2">
        <label for="WerkprocesFilter" class="form-label">Selected Workprocess:</label>
        <select id="WerkprocesFilter" v-model="currentWerkProces" @change="filterData" class="form-select">
          <!-- <option v-for="beoordeling in uniqueBeoordelingen" :key="beoordeling" :value="beoordeling">{{ beoordeling }}          </option> -->
          <option v-for="werkproces, index in data[0].werkproces" :key="werkproces" :value="index">{{
            werkproces.description }}</option>
        </select>
      </div>
      <div class="col-md-6 text-end">
        <div class="d-flex justify-content-end">
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageCompleted, '#007bff')"></div>
            <div class="chart-label">Completed</div>
          </div>
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageVoldoende, '#ffc107')"></div>
            <div class="chart-label">Voldoende</div>
          </div>
          <div class="chart-container me-3">
            <div class="donut-chart" :style="getDonutStyle(percentageGoed, '#28a745')"></div>
            <div class="chart-label">Goed</div>
          </div>
          <div class="chart-container">
            <div class="donut-chart" :style="getDonutStyle(percentageOnvoldoende, '#dc3545')"></div>
            <div class="chart-label">Onvoldoende</div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <div class="table-responsive">
          <table class="table table-striped table-hover table-bordered w-100">
            <thead class="bg-light-blue text-white">
              <tr>
                <th @click="sort('beoordelingscriteria')" class="cursor-pointer">
                  Course
                  <i :class="getSortIcon('beoordelingscriteria')"></i>
                </th>

                <th @click="sort('beoordeling')" class="cursor-pointer">
                  Beoordeling
                  <i :class="getSortIcon('beoordeling')"></i>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in paginatedData" :key="item.id" :class="getRowClass(item.beoordeling)">
                <td>
                  <RouterLink class="nav-link" :to="`/rubric/${item.beoordelingscriteria}`">
                    {{ item.beoordelingscriteria }}
                  </RouterLink>
                </td>
                <td>
                  <span :class="getBadgeClass(completionCriteriaTracker(item.criteria) / item.criteria.length)">{{ completionCriteriaTracker(item.criteria) }}  / {{ item.criteria.length }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <nav aria-label="Page navigation">
          <ul class="pagination justify-content-center">
            <li class="page-item" :class="{ disabled: currentPage === 1 }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)">Previous</a>
            </li>
            <li v-for="page in totalPages" :key="page" class="page-item" :class="{ active: currentPage === page }">
              <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
            </li>
            <li class="page-item" :class="{ disabled: currentPage === totalPages }">
              <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)">Next</a>
            </li>
          </ul>
        </nav>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const data = ref([
  {
    werkproces: [
      {
        description: "Jaar 1",
        criteria: [
          {
            id: 1, beoordelingscriteria: 'IntroICT', fase: 'Ontwikkeling', criteria: [
              { id: 1, project: 'INTROICT-1', verantwoording: '• Deelt een complexe ICT-situatie in in onderdelen en kan de samenhang tussen deze onderdelen benoemen (denk aan datastromen, communicatiestromen, verantwoordelijkheden, et cetera) en vastleggen in een model.\n• Onderbouwt keuzes bij het opstellen van een ICT-model.\n', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'INTROICT-2', verantwoording: '• Doet gestructureerd onderzoek naar hoe ICT wordt toegepast binnen een bedrijf of organisatie.\nSelecteert een of meerdere passende onderzoekstechnieken uit de ICT-methodenkaartenset en past deze op de juiste manier toe.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'INTROICT-3', verantwoording: 'Formuleert eigen doelstellingen rondom opleiding en carrière en onderbouwt dit met argumenten.\nPresenteert doelgroepgericht over de resultaten van het uitgevoerde onderzoek.\nRapporteert over het uitgevoerde onderzoek waarbij rekening wordt gehouden met de doelgroep.\nSchrijft conform de controlekaart.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 2, beoordelingscriteria: 'OOPD', fase: 'Uitvoerings', criteria: [
              { id: 1, project: 'OOPD-1', verantwoording: 'I applied innovative solutions to solve problems.', linked_project: 'Ontwikkeling', feedback: 'Creative approach and well-executed.', beoordeling: 'Goed' },
              { id: 2, project: 'OOPD-2', verantwoording: 'I ensured all tasks were completed on time.', linked_project: 'Uitvoerings', feedback: 'Timely completion, but quality can improve.', beoordeling: 'Voldoende' },
              { id: 3, project: 'OOPD-3', verantwoording: 'I reviewed all work thoroughly before submission.', linked_project: 'Afronding', feedback: 'Thorough review, excellent quality.', beoordeling: 'Goed' },
              { id: 4, project: 'OOPD-4', verantwoording: 'I faced challenges but overcame them with persistence.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Goed' },
            ],
          },
          {
            id: 3, beoordelingscriteria: 'EPD22', fase: 'Afronding', criteria: [
              { id: 1, project: 'EPD-1', verantwoording: 'Leest sensoren op de juiste manier uit, waardoor de gelezen waarden op de juiste manier kunnen worden verwerkt in de software.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'EPD-2', verantwoording: 'Prioriteert de requirements met de MoSCoW-methode.\nSchrijft conform de eisen van de controlekaart.\nSchrijft volledige, eenduidige en concrete requirements voor het te ontwikkelen systeem.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'EPD-3', verantwoording: 'Licht functies en globale variabelen toe.\nLicht gemaakte beslissingen in het ontwerp toe en onderbouwt deze.\nMaakt een compleet, overzichtelijk en leesbaar aansluit- en bedradingsschema voor het embedded systeem.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'EPD-4', verantwoording: 'Licht gemaakte beslissingen in de geschreven software toe en onderbouwt deze.\nProgrammeert een statemachine uit volgens het aangereikte implementatie-sjabloon.\nSchrijft leesbare en onderhoudbare software volgens de aangereikte regels, zonder zelf objectgeoriënteerde software te schrijven.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'EPD-7', verantwoording: 'Houdt in de uitwerking (zowel in functioneel en technisch ontwerp als programmacode) rekening met de traagheid van actuatoren.\nSluit sensoren en actuatoren op de juiste manier aan binnen het embedded systeem.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'EPD-8', verantwoording: 'Implementeert software-timers, dus zonder gebruik van de delay-functie of een equivalent daarvan, om meerdere taken schijnbaar gelijktijdig te laten uitvoeren.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 4, beoordelingscriteria: 'DBRP', fase: 'Voorbereiding', criteria: [
              { id: 1, project: 'DBRP-1', verantwoording: '• Gebruikt de juiste functies van de rapportagesoftware voor automatische groepering en subtotalen en overall totaal.\n• Kiest in het ontwerp een grafiektype passend bij de kpi.\n• Realiseert de in de informatiebehoefte geformuleerde keuzeopties.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'DBRP-2', verantwoording: '• Formuleert een KPI die voldoet aan de eisen. op basis van de informatiebehoefte.\n• Haalt alle benodigde informatie op uit de database en niet meer dan nodig middels een of meerdere bronquery\'s.\n• Realiseert een kpi of informatiebehoefte met door de gebruiker genoemde dimensies om daarmee tot de gewenste waarden te beperken.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'DBRP-3', verantwoording: '• Realiseert voor ieder rapport en grafiek tenminste één voorbeeldscreenshot waarin alle in de informatiebehoefte genoemde dimensies, meet- en grenswaarden en keuzeopties te zien zijn.\n• Schrijft bij ieder screenshot een SELECT-query waarin precies de betreffende dimensies, meetwaarden en keuzeopties zijn opgenomen.\n• Schrijft conform de eisen van de controlekaart.\n• Zorgt ervoor dat de met de SELECT-query ophaalde meetgegevens overeen komen met de gegevens in de screenshot.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 6, beoordelingscriteria: 'FAT', fase: 'Uitvoerings', criteria: [
              { id: 1, project: 'FAT-1', verantwoording: '• Beoordeelt de noodzaak van de requirements op een relevante manier.\n• Beschrijft relevante user requirements voor de informatiebehoefte.\n• Formuleert user requirements in de vorm van userstory\'s.\n• Koppelt de in de toelichting bij het usecasemodel benoemde requirements aan de juiste usecases.\n• Legt de systeemgrens van het usecasemodel vast.\n• Positioneer zowel primaire als ondersteunende actoren in het usecasediagram.\n• Verbindt de juiste actor(en) met de usecase.\n• Voorkomt eventuele redundantie bij usecases en actoren.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'FAT-2', verantwoording: '• Formuleert Actor, Naam, Stakeholders op correcte wijze. Beschrijft middels pre- en post-condities de toestand van een systeem\n• Geeft de plaats van included en extended usecases in de usecasebeschrijving op correcte wijze aan.\n• Onderkent en beschrijft alternatieve scenario\'s.\n• Scheidt duidelijk de verantwoordelijkheid van de actor en die van het systeem.\n• Schrijft conform de eisen van de controlekaart.\n• Verwerkt consequenties van user requirements in de usecase.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'FAT-3', verantwoording: 'Benoemt attributen van de concept-klassen.\nModelleert en documenteert associaties tussen de concept-klassen.\nOnderkent concept-klassen en geeft deze een juiste naam in de vorm van een zelfstandig naamwoord in enkelvoud.\nVoegt extra gegevens voortvloeiend uit associaties toe aan het model (Bij veel op veel associaties is een concept toegevoegd met attributen).\nVoorziet het businessclassdiagram van een verklarende toelichting.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'FAT-4', verantwoording: '• Verifieert de consistentie tussen het activitydiagram en de usecase.\n• Verifieert de grammatica van het activitydiagram.\n• Werkt alternatieve scenario\'s uit.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'FAT-5', verantwoording: '• Legt noodzakelijke scenario\'s voor testmaat-1 vast.\n• Onderkent de deelpaden in het activitydiagram.\n• Schrijft conform de eisen van de controlekaart.\n• Werkt mogelijke scenario\'s uit.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 8, beoordelingscriteria: 'SPAD', fase: 'Voorbereiding', criteria: [
              { id: 1, project: 'SPAD-1', verantwoording: 'De oplossingsalgoritmen en softwarearchitectuur staan beschreven in een professioneel document dat voldoet aan de controlekaart.\nDe oplossingsalgoritmen zijn volledig en passend bij de functionele eisen.\nEr is een passende softwarearchitectuur beschreven.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'SPAD-2', verantwoording: 'De juiste datatypen worden gebruikt(waaronder de verplichte array).\nEvents worden correct toegepast.\nGegeven requirements zijn conform de MoSCoW methode gerealiseerd.\nHerhalings- en controlestructuren zijn correct toegepast.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'SPAD-3', verantwoording: 'Daar waar van toepassing zijn methoden met parameters van het juiste datatype gedefinieerd.\nDaar waar van toepassing zijn methoden met returnwaarden van het juiste datatype gedefinieerd.\nDe code is leesbaar en geschreven conform voorgeschreven coderingsstandaarden.\nEr is een volledige decompositie gedaan die vertaald is naar methoden.\nEr is geen overbodige of herhalende code.\nMethoden zijn goed gestructureerd en conform de softwarearchitectuur onderverdeeld.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'SPAD-4', verantwoording: 'De tests zijn uitgevoerd met concrete testdata en het testresultaat is beschreven in een opleverdocument dat voldoet aan de controlekaart.\nEr is een concreet testplan opgesteld met testcases die de functionele eisen voldoende afdekken.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 9, beoordelingscriteria: 'SPB', fase: 'Ontwikkeling', criteria: [
              { id: 1, project: 'SPB-1', verantwoording: 'Begrijpt de scope van variabelen in programmeercode.\nBegrijpt het verschil tussen declareren en initialiseren.\nIs in staat elk van de datatypen te declareren en te initialiseren.\nKiest het juiste datatype bij een gegeven toepassing.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'SPB-2', verantwoording: 'Is in staat om een onderbouwde keuze te maken tussen beide opties.\nIs in staat om elk van deze lussen zonder hulp te implementeren.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'SPB-3', verantwoording: 'Is in staat om booleaanse expressies te lezen en daarvan het resultaat te bepalen.\nIs in staat om zelf de gegeven conditionele constructies implementeren.\nKan zelfstandig eigen booleaanse expressies schrijven.\nKent de verschillende operatoren en weet die toe te passen.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'SPB-4', verantwoording: 'Is in staat om de verantwoordelijkheden van (een gegeven stuk) code te herkennen en te benoemen.\nIs in staat om, op basis van gegeven verantwoordelijkheden, code gescheiden te implementeren in eenvoudige functies.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'SPB-5', verantwoording: 'Kan een gegeven oplossingsalgoritme vertalen naar de in deze OWE-beschrijving genoemde programmeerconcepten.\nKan vanuit een gegeven probleemstelling de belangrijkste oplossingsalgoritmen uitschrijven.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'SPB-6', verantwoording: 'Is in staat de Processing API te gebruiken bij het schrijven van een eigen programma.\nKan helder en eenduidig aangeven wat de Processing API is, waar je deze kunt vinden en wat je er kunt vinden.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },

            ],
          },
          {
            id: 11, beoordelingscriteria: 'WTUX', fase: 'Afronding', criteria: [
              { id: 1, project: 'WTUX-1', verantwoording: 'De specificatie is volledig verwerkt in de wireframes.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'WTUX-2', verantwoording: 'De formulieren zijn voorzien van validatie en werken correct.\nDe website heeft syntactisch correcte HTML-broncode.\nDe website maakt gebruik van semantische HTML-elementen.\nDe website reageert zoals ontworpen in de wireframes.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'WTUX-3', verantwoording: 'De stylesheets maken correct gebruik van herbruikbare onderdelen.\nDe website is uitgewerkt en vormgegeven met CSS zoals ontworpen in de wireframes.\nDe website is vormgegeven met een grote verscheidenheid aan CSS-technieken.\nDe website maakt optimaal gebruik van recente lay-outtechnieken zoals flexbox of grid.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
            ],
          }
        ]
      },
      {
        description: "Jaar 2",
        criteria: [
          {
            id: 1, beoordelingscriteria: 'OSM-MO', fase: 'Ontwikkeling', criteria: [
              { id: 1, project: 'OSM-MO-1', verantwoording: '• Gebruikt en beschrijft op correcte wijze de UML syntax van een use case diagram (inclusief visualisatie).\n• Kan verwoorden hoe het ontwerpproces zich verhoudt tot het gehele software engineeringstraject.\n• Stelt use cases UI-onafhankelijk op en maakt gebruik van black-box formulering.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'OSM-MO-2', verantwoording: '• Kan motiveren waarom en wanneer we een modelling-taal en in het bijzonder UML gebruikt wordt.\n• Kan software/hardware-systemen benoemen met daarbij een use-case.\n• Kan uitleggen wat het verschil tussen abstract en concreet is en wat dat betekent in het kader van een softwareontwerp.\n• Maakt gebruik van tooling die beschikbaar is voor het maken van een ontwerp met behulp van UML.\n', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'OSM-MO-3', verantwoording: '• Gebruikt en beschrijft op correcte wijze de UML syntax van een sequence-diagram (inclusief visualisatie).', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'OSM-MO-4', verantwoording: '• Gebruikt en beschrijft op correcte wijze de UML syntax van een activity- en state-diagram (inclusief visualisatie).', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'OSM-MO-5', verantwoording: '• Gebruikt en beschrijft op correcte wijze de UML syntax van een class-diagram (inclusief visualisatie).\n• Geeft visibility aan van alle operaties en attributen in een class diagram en toont welke rollen van een associatie navigeerbaar zijn.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'OSM-MO-6', verantwoording: '• Definiëert zelf stereotypes en gebruikt deze in een UML-diagram.\n• Maakt gebruik van voorgedefinieerde UML-stereotypes.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 7, project: 'OSM-MO-7', verantwoording: '• Gebruikt en beschrijft op correcte wijze de UML syntax van een deployment- en component- en package-diagram (inclusief visualisatie).', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 8, project: 'OSM-MO-8', verantwoording: '• Kan bedenken/identificeren wat een mogelijk onderdeel/component is van een systeem.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'OSM-MO-9', verantwoording: '• Vertaalt de UML-syntax van de verschillende diagrammen naar C++-code met herkenbare OO-constructen.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 10, project: 'OSM-MO-10', verantwoording: '• Kan de verschillende diagrammen binnen UML benoemen en weet hoe ze gecategoriseerd zijn.\n• Kan het verschil aangeven tussen semantiek en syntaxis is en wat dat betekent in het kader van een softwareontwerp.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 2, beoordelingscriteria: 'OSM-SO', fase: 'Uitvoerings', criteria: [
              { id: 1, project: 'OSM-SO-1', verantwoording: '• Installeert de GNU C++ compiler op een van de platforms Windows, Linux, Mac.\n• Installeert, configureert en gebruikt Eclipse. Het werk wordt ingeleverd als Eclipse-project', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'OSM-SO-2', verantwoording: '• Gebruikt SVN of Git en levert het materiaal in in de vorm van een repository-export.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'OSM-SO-3', verantwoording: '• Gebruikt een van de unit-test frameworks Boost Test Library, CppUnit, Google Test.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'OSM-SO-5', verantwoording: '• Gebruikt relevante elementen uit tenminste de volgende C++ standaard libraries: utility library, string library, container library, algoritme library, iterator library, reguliere expressie library, atomaire operaties library, thread support library.\n• Past, waar relevant, van C++ de volgende concepten op correcte wijze toe: classes (constructors, destructor, operators, accessors, mutators, member data en functions) en objecten, virtuele member functions, function overloading, polymorphisme, call by value en reference, const- correctness, pointers, functie-pointers, functie objecten (functoren), lambda functies, excepties, splitsing van code in implementatie en interface file (header file).', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'OSM-SO-6', verantwoording: '• Implementeert het ontwerp in C++ zoals vastgelegd in UML, conform de UML run-time semantiek.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'OSM-SO-8', verantwoording: '• Gebruikt best practices op het gebied van refactoring bij het verbeteren van de implementatie naar aanleiding van de beoordeling de kwaliteit van de code en implementatie van het ontwerp.\n• Houdt rekening met de complexiteit van algoritmen (“Big-O-Notation”) bij de relevante QoS-aspecten van de applicatie.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 7, project: 'OSM-SO-9', verantwoording: '• Maakt gebruik van exception handling waar niet kan worden voldaan aan het gestelde functie-contract.\n• Schrijft doel- en doelgroepgericht, en de tekst voldoet aan de eisen van de AIM-controlekaart (voorheen ICA-controlekaart). \n • Stelt contracten vast voor alle functies in de vorm van precondities, gedrag en postcondities. \n• Stelt unit-tests waarbij wordt gecontroleerd op de bij functies opgestelde contracten\n• Toont aan dat de applicatie voldoet aan de gestelde functionele- en niet functionele eisen\n• Voert gericht onderzoek uit naar alternatieve algoritmes waarbij de bruikbaarheid wordt beargumenteerd op basis van voor de gestelde eisen relevante aspecten.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 8, project: 'OSM-SO-10', verantwoording: '• Licht de testen mondeling toe en motiveert eventuele afwijkingen van de gestelde eisen.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'OSM-SO-11', verantwoording: '• Gebruikt GNU Autotools als build management systeem \n• Installeert externe libraries zoals bijvoorbeeld Boost en/of een GUI library.\n• Maakt UML diagrammen met een UML editor die tenminste UML versie 2.4.1 ondersteunt', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 10, project: 'OSM-SO-14', verantwoording: '• Houdt rekening met hetgeen overgebracht moet worden bij het opstellen van een UML-diagram.\n• Voert een analyse uit op al bestaande software en legt informatie vast in UML-diagrammen.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 11, project: 'OSM-SO-15', verantwoording: '• Gebruikt relevante elementen uit tenminste de volgende C++ standaard libraries: utility library, string library, container library, algoritme library, iterator library, reguliere expressie library, atomaire operaties library, thread support library.\n• Maakt gebruik van Boost.Asio bij het realiseren van synchrone en asynchrone operaties in relatie tot netwerken, seriële poorten, timers en signals.\n• Maakt gebruik van een GUI bibliotheek voor het realiseren van een grafische gebruikersinterface.\n• Past van C++ de volgende concepten op correcte wijze toe: classes(constructors, destructor, operators, accessors, mutators, member data en functions) en objecten, virtuele member functions, function overloading, polymorphisme, call by value en reference, const- correctness, pointers, functie - pointers, functie objecten(functoren), lambda functies, excepties, splitsing van code in implementatie en interface file (header file).', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 12, project: 'OSM-SO-16', verantwoording: '• Implementeert het ontwerp in C++ zoals vastgelegd in UML, conform de UML run-time semantiek.', linked_project: 'Afronding', feedback: 'temp', beoordeling: 'Goed' },
              { id: 13, project: 'OSM-SO-17', verantwoording: '• Gebruikt herkent en gebruikt relevante patterns of idioms uit tenminste de volgende subset: Creational patterns (Factory method, Singleton, Structural patterns, Proxy), Behavioural patterns (Chain of responsibility, Observer, State), Concurrency patterns (Active Object, Guarded suspension, Messaging design pattern).', linked_project: 'Voorbereiding', feedback: 'temp', beoordeling: 'Goed' },
              { id: 14, project: 'OSM-SO-18', verantwoording: '• Beoordeelt de realisatie van een applicatie op tenminste de onderety, reliability, security, quality, scalability, het gebruik van architectural patterns, het gebruik van design patterns.\nGebruikt best practices op het gebied van refactoring bij het verbeteren van de implementatie naar aanleiding van de beoordeling de kwaliteit van de code en implementatie van het ontwerp.\nGebruikt een expliciete style guide bij het beoordelen van de kwaliteit van de code.De style guide wordt door de student zelf opgesteld op basis van bekende professionele style guides.', linked_project: 'Afronding', feedback: 'temp', beoordeling: 'Goed' },
              { id: 15, project: 'OSM-SO-19', verantwoording: '• Legt documentatie vast op (in ieder geval) functieniveau in de vorm van een contract (pre-condities, gedrag, post-condities).\n• Toont de gestelde functionele eisen aan m.b.v.een demonstratie en mondelinge toelichting.', linked_project: 'Voorbereiding', feedback: 'temp', beoordeling: 'Goed' },
              { id: 16, project: 'OSM-SO-21', verantwoording: '• Beoordeelt de kwaliteit van documentatie en realisatie en verwoord dit in een verslag.\n• Licht in een schriftelijk rapport toe welke onderdelen van het documentatie gewijzigd zouden moeten worden om de kwaliteit te verbeteren.\n• Schrijft doel - en doelgroepgericht, en de tekst voldoet aan de eisen van de AIM - controlekaart(voorheen ICA - controlekaart).', linked_project: 'Afronding', feedback: 'temp', beoordeling: 'Goed' },
            ],
          },
          {
            id: 3, beoordelingscriteria: 'OSM-Pr', fase: 'Afronding', criteria: [
              { id: 1, project: 'OSM-Pr-1', verantwoording: 'Bewaakt de voortgang van het project aan de hand van de in het plan van aanpak opgenomen planning en stuurt bij als dat nodig is(eigen bijdragen en bijdrage team). (Groep - TT, Groep - Eind)\n Stemt zijn eigen bijdrage aan het projectresultaat af met de rest van het team.Deze bijdrage wordt zichtbaar in de projectplanning. (Ind - TT, Ind - Eind)\n Stuurt actief en aantoonbaar het verloop van het project(voortgang en evaluatie) door gebruik te maken van de beheersfactoren. (Groep - TT, Groep - Eind)\n Vult disciplines, rollen en werkproducten in volgens een proces gebaseerd op scrum. (Groep - TT, Groep - Eind)', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'OSM-Pr-2', verantwoording: 'Bewaakt de bruikbaarheid en leesbaarheid van de analysedocumentatie rekening houdend met eisen en wensen uit de opdracht(en het plan van aanpak) en de bruikbaarheid voor het ontwerp(waarbij technische beperkingen en mogelijkheden van het systeem in acht worden genomen). (Groep - TT, Groep - Eind)\n Levert een representatieve bijdrage aan de kwantiteit, met voldoende complexiteit van de analysedocumentatie. (Ind - TT, Ind - Eind)\n Voert een analyse uit waarbij rekening wordt gehouden met de eisen en wensen uit de opdracht en bruikbaarheid voor het ontwerp(technische beperkingen en mogelijkheden van het systeem). (Ind - TT, Ind - Eind)', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'OSM-Pr-3', verantwoording: 'Bewaakt de bruikbaarheid en leesbaarheid van de onderzoeksdocumenten rekening houdend met relevantie en concrete bijdrage aan het project en het hanteren van een gestructureerde aanpak en gebruik van onderzoekstechnieken. (Groep - TT, Groep - Eind)\n Levert een representatieve bijdrage, met voldoende complexiteit aan de uitgevoerde onderzoeken. (Ind - TT, Ind - Eind)\n Voert gestructureerd en doelgericht een(deel van een) onderzoek uit ten behoeve van het project, waarbij onderzoeksmethoden en technieken correct zijn toegepast. (Ind - TT, Ind - Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'OSM-Pr-4', verantwoording: 'Bewaakt actief de bruikbaarheid en leesbaarheid ontwerpdocumenten rekening houdend met aansluiting op de analysedocumentatie en(technische) bruikbaarheid voor implementatie. (Groep - TT, Groep - Eind)\n Levert een representatieve bijdrage aan de kwantiteit, met voldoende complexiteit van de ontwerpdocumentatie(Ind - TT, Ind - Eind)\n Maakt een ontwerp voor(een deel van) de software van een systeem waarbij rekening wordt gehouden met de benodigde functionaliteit, verdere analysedocumentatie en bruikbaarheid voor implementatie. (Ind - TT, Ind - Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'OSM-Pr-5', verantwoording: 'Maakt zinnig gebruik van relevante ontwikkel - en \'collaborative\' - tools en houdt daarbij rekening met de eisen zoals gesteld in het plan van aanpak. (Ind - TT, Ind - Eind)\n Zet collaborative tools in voor beheersen van het groepsproces en het bewaken van de geleverde productkwaliteit. (Groep - TT, Groep - Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'OSM-Pr-6', verantwoording: 'Bewaakt de voortgang en bruikbaarheid van de implementatie(code) en bijbehorende code - documentatie en verifieert of deze in lijn is(en blijft) met de ontwerp - documentatie. (Groep - TT, Groep - Eind)\n Implementeert software in overeenstemming met het ontwerp en voorziet deze van concrete code - documentatie. (Ind - TT, Ind - Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 7, project: 'OSM-Pr-7', verantwoording: 'Bewaakt de kwaliteit van de ontwikkelde software rekening houdend met de aanwezige onderbouwing bij het ontwerp en de daarin gebruikte design - patterns, maar ook de resultaten van code - analysetools. (Groep - TT, Groep - Eind)\n De student levert een representatieve en kwantitatieve bijdrage aan de kwaliteit van de software. (Ind - TT, Ind - Eind)\n Past waar nodig documentatie of code aan om de kwaliteit te verbeteren. (Ind - TT, Ind - Eind)\n Voert kwaliteitscontroles uit op analyse, ontwerp, code, en overige documentatie en gebruikt hierbij(waar mogelijk) analysetools. (Ind - TT, Ind - Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 8, project: 'OSM-Pr-8', verantwoording: 'Bewaakt de werking van het gerealiseerde systeem(en onderliggende deelsystemen) op basis van de test - coverage en test - resultaten, waarbij ook wordt gecontroleerd of de testen aansluiten bij de ontwerpdocumentatie. (Groep - TT, Groep - Eind)\n Levert een representatieve bijdrage aan de kwantiteit, met voldoende complexiteit, van de testen. (Ind - TT, Ind - Eind)\n Test de werking van het systeem op gestructureerde wijze, waarbij rekening wordt gehouden met relevante delen van het ontwerp en code(voor zover mogelijk) wordt getest aan de hand van unit - tests. (Ind - TT, Ind - Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'OSM-Pr-9', verantwoording: 'Analyseert de samenwerking in de groep, stuurt en analyseert interventies, om een groepsopdracht tot een goed einde te brengen. (Groep - TT, Groep - Eind)\n De student levert een representatieve bijdrage aan het groepsproces. (Ind - TT, Ind - Eind)\n Gebruikt gesprekstechnieken correct in situaties zoals planningpoker, sprint reviews, code reviews en retrospectives. (Ind - TT, Ind - Eind)\n Schept aan het begin van het project aantoonbaar en actief voorwaarden voor een optimale interactie in het team. (Ind - TT)\n Voert zijn rol zoals deze in scrum beschreven staat correct uit ten behoeve van de groep. (Groep - TT, Groep - Eind)\n Voert zijn rol zoals deze in scrum en het plan van aanpak zijn beschreven staat correct uit. (Ind - TT, Ind - Eind)\n Werkt tijdens het gehele ontwikkelproces indien wenselijk of noodzakelijk samen met andere leden van het team. (Ind - TT, Ind - Eind)\n Werkt volgens afspraken die gemaakt zijn in het plan van aanpak. (Groep - TT, Groep - Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 10, project: 'OSM-Pr-10', verantwoording: 'Deelt gedurende het proces opgedane nieuwe kennis en vaardigheden met de groep. (Groep - TT, Groep - Eind)\n Stuurt bewust in zijn eigen beroepsmatige ontwikkeling, formuleert hier vragen bij en reflecteert op zijn ontwikkeling(Ind - TT, Ind - Eind)\n Verwerft gedurende het proces waar nodig nieuwe kennis en vaardigheden. (Ind - TT, Ind - Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
            ],
          },
          {
            id: 4, beoordelingscriteria: 'IoT-S', fase: 'Voorbereiding', criteria: [
              { id: 1, project: 'IoT-SW-1', verantwoording: 'Richt een werkende ontwikkelomgeving voor de benodigde programmeertalen en doelplatformen.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'IoT-SW-2', verantwoording: 'Identificeert timing-aspecten en maakt keuzes hoe aan deze te voldoen.\nKiest de juiste softwareoplossing bij beperkingen van hardware.\nRealiseert een oplossing met een embedded controller, waarbij beargumenteerd gebruik gemaakt wordt van hardware- en software-timers, pointers, geheugen-(de/re)allocatie, queues en interrupts.\nSchrijft op een eenduidige manier code, past coding guidelines toe.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'IoT-SW-3', verantwoording: 'Realiseert software in Python (imperatief en object-geörienteerd).\nSchrijft op een eenduidige manier werkende code, past coding guidelines toe.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'IoT-SW-4', verantwoording: 'Kan data serialiseren en deserialiseren met behulp van JSON en XML.\nMaakt een eigen implementatie van een webserver op een embedded systeem zodanig dat het HTTP-protocol wordt ondersteund.\nMaakt in programmatuur gebruik van REST.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'IoT-SW-5', verantwoording: 'Logt bijzondere situaties.\nSchrijft unittests, schrijft integratietests op basis van een protocolbeschrijving, voert deze tests uit.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'IoT-SW-6', verantwoording: 'Gebruikt een softwarebibliotheek om matrixberekeningen op grote matrices uit te voeren (optellen, aftrekken, vermenigvuldigen, transpose, determinant, inverse).\nSchrijft software om efficiënt statistische waarden als gemiddelde en standaarddeviatie te kunnen berekenen.\nSchrijft software om lineaire regressie voor eerste-, tweede- en derdegraadsfuncties te kunnen berekenen.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 7, project: 'IoT-SW-8', verantwoording: 'Legt de resultaten van een prototypetraject vast, houdt voortgangsverslag bij, overweegt alternatieven en legt deze vast, verantwoordt met argumenten, zowel schriftelijk als mondeling, de genomen keuzes.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' }
            ],
          },
          {
            id: 5, beoordelingscriteria: 'IoT-TE', fase: 'Ontwikkeling', criteria: [
              { id: 1, project: 'IoT-TECH-1', verantwoording: 'Richt de ontwikkelomgeving van een managed programmeertaal zodanig in dat de uiteindelijke software voor de embedded controllers zowel vanuit de ontwikkelomgeving als via het netwerk op afstand kan worden geladen via een bootloader. De uiteindelijke software kan ook volledig zelfstandig functioneren.\nRicht een werkende ontwikkelomgeving van een managed programmeertaal in, en geeft een overzicht van de hardware-architectuur met alle systeem-hardware, aansluitingen, systeemsoftware, de bronnen en de configuratieparameters, die afwijken van de standaardwaarde.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'IoT-TECH-2', verantwoording: 'Heeft kennis van het OSI-lagenmodel, de interfacing tussen lagen, de algemene pakketopbouw, de toevoeging van headers per laag, overhead, het verschil tussen directe en virtuele verbindingen.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'IoT-TECH-3', verantwoording: 'Maakt met hoofd- en deelonderzoeksvragen kwalitatieve en kwantitatieve vergelijkingen.\nOntwerpt een testapplicatie en -opstelling voor een sensor en/of actuator en kan deze realiseren.\nPast relevante onderzoeksmethoden correct toe en onderbouwt de keuze voor de methoden.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'IoT-TECH-4', verantwoording: 'Kent de basisprincipes van microcontrollers.\nOntwerpt en realiseert een voorbeeldtoepassing en voert met zelf opgestelde criteria een vergelijkend onderzoek uit.\nPast interrupts, RTOS en super-loop toe in oplossingen en onderbouwt welke gevolgen dat heeft voor het systeem gedrag.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'IoT-TECH-5', verantwoording: 'Onderbouwt gemaakte keuzes en zorgt door volledige documentatie voor reproduceerbare resultaten.\nSchrijft correct en doel- en doelgroepgericht.\nSchrijft een verslag, bestaande uit meerdere deelonderzoeken, en met een duidelijke en logische structuur.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'IoT-TECH-6', verantwoording: 'IJkt en/of kalibreert sensoren.\nImplementeert meerdere sensor- en actuatorprotocollen.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 7, project: 'IoT-TECH-7', verantwoording: 'Bepaalt van een gegeven signaal de spanning, de frequentie en de golfvorm, met daartoe gekozen apparatuur.\nGebruikt meetapparatuur zoals multimeter.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 8, project: 'IoT-TECH-8', verantwoording: 'Geeft m.b.v. logic analyzer uitleg over gebruikte protocollen en onderlinge timing.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'IoT-TECH-9', verantwoording: 'In staat om (op basis van documentatie) zonder low-level drivers oplossingen te realiseren waarmee o.a. realtime eigenschappen geanalyseerd kunnen worden.\nOntwerpt en realiseert een voorbeeldtoepassing voor een nieuwe technologie, module of ontwikkelomgeving en kan met zelf opgestelde criteria een vergelijkend onderzoek uitvoeren.\nPast debugging tools en methodieken toe om fouten en bugs te identificeren en repareren.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 11, project: 'IoT-TECH-11', verantwoording: 'Implementeert één of meer cryptografische algoritmen op embedded systemen en beoordeelt deze op snelheid en geheugengebruik.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' }
            ],
          },
          {
            id: 6, beoordelingscriteria: 'IoT-Pr', fase: 'Uitvoerings', criteria: [
              { id: 1, project: 'INTOFT25-1', verantwoording: 'Je bent fysiek aanwezig gedurende het project en je stelt je begeleidbaar op. Je betrekt betrokkenen bij de keuzes in de voortgang van het project.\n Je draagt kwantitatief en kwalitatief evenredig bij en neemt medeverantwoordelijkheid voor het volledige plan van aanpak.\n Je kent de mechanismen die in prototyping worden gebruikt om bij te sturen op proces en product, en je gebruikt deze mechanismen actief.\n Je licht in voortgangs- en overdrachtpresentatie doel- en doelgroepgericht een stuk van je eigen werk en de stand van zaken met betrekking tot je leerdoelen toe.\n Je schrijft een PvA waarin je op te lossen problemen verheldert, waarin je potentiële blokkades identificeert en waarin je een concreet en acceptabel plan opstelt.\n Je vertaalt je concrete resultaten naar de situatie van de opdrachtgever.\n Je verzorgt aan het einde van het project een zinvolle overdrachtspresentatie.\n Je verzorgt halverwege het project een zinvolle voortgangspresentatie.\n Je werkt actief mee aan het doelgericht laten verlopen van de in het project benodigde ceremonies.\n Je werkt op dagelijkse basis actief samen met je team, gebruikmakend van de toolstack en technieken zoals feedback geven en ontvangen op producten en gedrag, conflicthantering, teameffectiviteit en gesprekstechnieken.\n', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.\n', beoordeling: 'Goed' },
              { id: 2, project: 'INTOFT25-2', verantwoording: 'Je bent transparant in de herkomst van je code: Code komt voort uit je eigen geestelijke inspanning óf is voorzien van bronvermelding.\n Je draagt er zorg voor dat de gedocumenteerde onderbouwing doel- en doelgroepgericht is.\n Je draagt kwantitatief en kwalitatief evenredig bij en neemt medeverantwoordelijkheid voor de code.\n Je draagt kwantitatief en kwalitatief evenredig bij en neemt medeverantwoordelijkheid voor de volledige onderbouwing in documentatie.\n Je formuleert onderzoeksvragen en kiest een passende onderzoeksmethode waarmee je de basis legt voor het kunnen uitvoeren van een doelgericht onderzoek.\n Je hanteert in overleg met de opdrachtgevers codeconventies.\n Je onderbouwt de situaties waarin je hiervan afwijkt.\n Je legt de bevindingen van een prototype reproduceerbaar vast in een onderzoeksrapport als beschreven in de projecthandleiding.\n Je legt de bevindingen van het project vast in het overdrachtsdocument zoals beschreven in de projecthandleiding.\n Je legt de tests die aan het prototype worden uitgevoerd vast in een testplan.\n Het resultaat van een test wordt vastgelegd in een testrapport. Je legt desgevraagd nut en functie van het ontwikkelde protocol uit. Je legt desgevraagd nut en functie van opgeleverde code uit. Tijdens het uitvoeren van taken leg je de voortgang vast in het logboek als beschreven in de projecthandleiding. Je stuurt op basis daarvan bij.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'INTOFT25-3', verantwoording: 'Je bouwt een embedded gedistribueerde oplossing met sensoren en actuatoren verbonden via control logic. Je onderbouwt aansluiting en gedrag van deze in je oplossing op basis van informatie uit relevante datasheets. Je houdt rekening met aangeboden en gevraagde quality of service met betrekking tot de volgende aspecten: capaciteit, real-time gedrag, verlies van berichten, betrouwbaarheid, redundancy, en configureerbaarheid. Je legt relatie tussen opstelling en situatie opdrachtgever uit. Je lost problemen in de in je opstelling systematisch op. Je verwerft zelfstandig relevante kennis en vaardigheden die nodig zijn om de opdracht met succes af te kunnen ronden.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.\n', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'INTOFT25-4', verantwoording: 'Je draagt kwantitatief en kwalitatief bij aan het gebruik van de procesondersteuning en neemt medeverantwoordelijkheid voor de kwaliteit ervan.\n Je maakt de onderlinge relatie tussen de verschillende delen in de inhoud van procesondersteuning traceerbaar.\n Je maakt gebruik van procestooling om voortgang van het project inzichtelijk te maken.\n Waar mogelijk en zinvol maak je gebruik van code-tools.\n', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.\n', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'INTOFT25-5', verantwoording: 'Je analyseert de samenwerking in de groep, bekijkt je eigen rol hierin en stuurt bij om de groepsopdracht tot een goed einde te brengen of trekt daaruit conclusies voor vervolgprojecten.\n Je formuleert een onderbouwd oordeel over de kwaliteit van de processen, met inachtneming van het doel van die processen, en doet daarvoor onderbouwde verbetervoorstellen of trekt daaruit conclusies voor vervolgprojecten.\n Je formuleert een onderbouwd oordeel over de kwaliteit van producten, met inachtneming van het doel van die producten, en doet daarvoor onderbouwde verbetervoorstellen.\n Je geeft, ontvangt en verwerkt feedback tijdens evaluaties van product of proces.\n Je laat je ontwikkeling ten aanzien van je twee persoonlijke leerdoelen zien en trekt daaruit conclusies voor vervolgprojecten.\n Je legt inhoudelijk uit waar je op dit moment staat ten opzichte van de kwalificaties van dit semester en wat je nog verder te ontwikkelen hebt.\n Je legt relevantie, oplossing en bruikbaarheid van zelfstandig verworven kennis en vaardigheden uit voor dit en latere projecten.\n Je onderbouwt kwantiteit en kwaliteit van je eigen bijdragen.\n', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.\n', beoordeling: 'Onvoldoende' }
            ],
          },
        ]
      }, {
        description: "Jaar 3",
        criteria: [
          {
            id: 2, beoordelingscriteria: 'WoR-Rbts', fase: 'Uitvoerings', criteria: [
              { id: 1, project: 'WoR-R-1', verantwoording: 'Gebruikt relevante natuurkunde uit onder andere de mechanica en elektrotechniek.\nGebruikt relevante toegepaste wiskunde uit onder andere de discrete wiskunde, calculus, algebra, goniometrie, meetkunde en topologie.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'WoR-R-2', verantwoording: 'Analyseert het gebruik van resources, het tijdaspect en de concurrency met de relevante elementen uit het UML real time resource modeling subprofile.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'WoR-R-3', verantwoording: 'Analyseert de scheduling en de performance met de relevante elementen uit het UML real time schedulability modeling subprofile.\nBeargumenteert de keuze tussen online en offline plannen.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'WoR-R-4', verantwoording: 'Analyseert relevante aspecten van een embedded systeem met beschrijvende statistiek.\nIdentificeert, kwantificeert en meet relevante aspecten van een embedded systeem.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'WoR-R-5', verantwoording: 'Geeft argumenten voor een concrete casus voor ontwerpbeslissingen met betrekking tot, synchrone en a-synchone communicatie, error detectie, fault tolerance en consensus.\nKiest, gegeven de requirements, de juiste schedulingstechniek en geeft de gevolgen ervan aan in relatie tot de requirements.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 6, project: 'WoR-R-8', verantwoording: 'Gebruikt relevante elementen uit tenminste de C++ standard libraries utility library, string library, container library en iterator library.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 7, project: 'WoR-R-9', verantwoording: 'Gebruikt relevante elementen uit tenminste de C++ standard libraries utility library, atomaire operaties library en thread support library.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'WoR-R-13', verantwoording: 'Analyseert de scheduling en de performance met de relevante elementen uit het UML real time schedulability modeling subprofile.\nBeargumenteert de keuze tussen online en offline plannen.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 10, project: 'WoR-R-14', verantwoording: 'Analyseert relevante aspecten van een embedded systeem met beschrijvende statistiek.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 11, project: 'WoR-R-15', verantwoording: 'Geeft argumenten voor een concrete casus voor ontwerpbeslissingen met betrekking tot, synchrone en a-synchone communicatie, error detectie, fault tolerance en consensus.\nKiest, gegeven de requirements, de juiste schedulingstechniek en geeft de gevolgen ervan aan in relatie tot de requirements.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 12, project: 'WoR-R-16', verantwoording: 'Geeft argumenten voor een concrete casus voor ontwerpbeslissingen met betrekking tot synchrone en a-synchone communicatie, error detectie, fault tolerance en consensus.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 13, project: 'WoR-R-17', verantwoording: 'Beoordeelt het ontwerp op tenminste de onderdelen compleetheid, consistentie, performance, timeliness, safety, reliability, security, quality, scalability, het gebruik van architectural patterns en het gebruik van design patterns.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
            ],
          },
          {
            id: 3, beoordelingscriteria: 'WoR-Wrld', fase: 'Afronding', criteria: [
              { id: 1, project: 'WoR-W-1', verantwoording: 'Analyseert het tijdaspect met behulp van de relevante elementen uit het UML Real Time Time Modeling Subprofile. Hierbij wordt onderscheid gemaakt tussen hard- en soft-realtime eisen.\nIdentificeert en benoemt QoS requirements en de koppeling met de subsystemen.\nMaakt een decompositie van use-cases voor de hardware subsystemen.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'WoR-W-2', verantwoording: 'Voert aan de hand van performance specificaties een analyse uit naar de QoS die hardware kan bieden. Hierbij komen timeliness properties en resource beschikbaarheid aan bod.\nLegt de bij de analyse gevonden timeliness properties vast in de UML-diagrammen met behulp van het time modeling sub-profile.\nLegt de gevonden QoS-onderdelen vast in de relevante UML-diagrammen (use cases, activity-, state- en timing diagrammen) en kan de hierbij gemaakte keuzes beargumenteren.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'WoR-W-4', verantwoording: 'Beargumenteert inzetbaarheid van hardware in het gewenste systeem op basis van gevonden QoS-mogelijkheden. Hierbij worden voor het modelleren van resources onderkend: (1) het door de hardware (sub)systemen aangeboden QoS is minstens even goed als vereiste QoS; (2) resource beschikbaarheid.\nBepaalt de grenzen en onzekerheden van een systeem, rekening houdend met soft- en hard-realtime zaken.\nBepaalt met behulp van statistiek wat de eigenschappen van een systeem zijn en legt hiermee de timeliness eigenschappen van het systeem vast. Hierbij worden begrippen gehanteerd als histogram, verdeling, modus, deviatie.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'WoR-W-5', verantwoording: 'Beargumenteert de keuze voor de fysieke hardwarearchitectuur en -configuratie aan de hand van consequenties voor realtime gedrag en resourcegebruik.\nBeargumenteert logische architectuur (packageniveau) ten aanzien van de hardwareaansturing op basis van systeemeisen.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'WoR-W-6', verantwoording: 'Biedt een inzicht in het gebruik van de interface op basis van uitwerking van verschillende scenario’s in sequence- en timingdiagrammen.\nMaakt een ontwerp voor de aansturing van de (gecombineerde) hardware in relevante UML-diagrammen, waarbij wordt voldaan aan alle QoS aspecten.\nMaakt gebruik van de relevante design patterns (adapter, observer, etc.) bij het opzetten van een interface en kan de hierbij gemaakte keuzes beargumenteren.\nOntwerpt een (abstracte) interface voor de aansturing van de (gecombineerde) hardware overeenkomt met de door het systeem gewenste interface. Niet essentiële zaken worden hierbij verborgen door encapsulatie.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 6, project: 'WoR-W-7', verantwoording: 'Voorziet het gemaakte UML-ontwerp van toevoeging van relevante zaken uit de UML Real Time subprofielen voor Time, Schedulability en Performance.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 7, project: 'WoR-W-9', verantwoording: 'Houdt bij de implementatie rekening met concurrency.\nImplementeert de gegeven hardwareaansturing zodanig, dat de implementatie overeenkomt met het ontwerp.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 8, project: 'WoR-W-10', verantwoording: 'Neemt de verschillende UML notaties uit de RT-subprofielen uit het ontwerp op in de implementatie en kan deze aanwijzen in de code.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'WoR-W-11', verantwoording: 'Laat zien dat de hardware kan worden uitgelezen en aangestuurd op basis van de afgesproken interface.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 10, project: 'WoR-W-12', verantwoording: 'Beargumenteert inzetbaarheid van hardware in het gewenste systeem op basis van gevonden QoS-mogelijkheden. Hierbij worden voor het modelleren van resources onderkend: (1) het door de hardware (sub)systemen aangeboden QoS is minstens even goed als vereiste QoS; (2) resource beschikbaarheid.\nBepaalt de grenzen en onzekerheden van een systeem, rekening houdend met soft- en hard-realtime zaken.\nBepaalt met behulp van statistiek wat de eigenschappen van een systeem zijn en legt hiermee de timeliness eigenschappen van het systeem vast. Hierbij worden begrippen gehanteerd als histogram, verdeling, modus, deviatie.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 11, project: 'WoR-W-13', verantwoording: 'Installeert, configureert en gebruikt een simulatieomgeving.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 12, project: 'WoR-W-14', verantwoording: 'Laat zien dat de hardware kan worden uitgelezen en aangestuurd op basis van de afgesproken interface.', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 13, project: 'WoR-W-15', verantwoording: 'Stelt aan de hand van gegeven UML-ontwerpen, voorzien van QoS-annotaties (RT-subprofielen), testcases op om het realtime gedrag te testen.\nToont op basis van uitgevoerde tests en metingen aan dat het systeem wel of niet voldoet aan de gestelde realtime eisen.', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 14, project: 'WoR-W-16', verantwoording: 'Schrijft een nog niet bestaande hardware-simulatiestub en integreert deze in een simulatiesysteem.\nSimuleert ontbrekende hardware met een software teststub met dezelfde interface.', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 15, project: 'WoR-W-17', verantwoording: 'Bouwt een virtuele wereld op in een simulatieomgeving en voert daarbinnen simulaties uit.\nDoet een beargumenteerde uitspraak over de bruikbaarheid en betrouwbaarheid van de uitgevoerde simulatie.', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' }
            ],
          },
          {
            id: 4, beoordelingscriteria: 'WoR-Pr', fase: 'Voorbereiding', criteria: [
              { id: 1, project: 'WoR-P-1', verantwoording: 'Beargumenteert de bruikbaarheid van de ontwikkelmethodiek op grond van de specifieke eigenschappen van het project. (Ind-TT, Ind-Eind)\nBewaakt de voortgang van het project aan de hand van de in het plan van aanpak opgenomen planning en stuurt bij als dat nodig is (eigen bijdragen en bijdrage team). (Groep-TT, Groep-Eind)\nLevert een kwalitatief en kwantitatief representatieve bijdrage aan het plan van aanpak, rekening houdend met de specifieke eigenschappen van het project, en kan deze verantwoorden. (Ind-TT)\nLevert een kwalitatief en kwantitatief representatieve bijdrage met voldoende complexiteit aan de processen en producten van RUP en Scrum, rekening houdend met de specifieke eigenschappen van het project. (Ind-TT, Ind-Eind)\nStuurt actief en aantoonbaar het verloop van het project (voortgang en evaluatie) door gebruik te maken van de beheersfactoren. (Groep-TT, Groep-Eind)', linked_project: 'Ontwikkeling', feedback: 'Excellent effort and attention to detail.', beoordeling: 'Goed' },
              { id: 2, project: 'WoR-P-2', verantwoording: 'Beargumenteert de bruikbaarheid van de ontwikkelmethodiek op grond van de specifieke eigenschappen van het project. (Ind-TT, Ind-Eind)\nBewaakt de bruikbaarheid en leesbaarheid van de analysedocumentatie rekening houdend met eisen en wensen uit de opdracht (en het plan van aanpak) en de bruikbaarheid voor het ontwerp (waarbij technische beperkingen en mogelijkheden van het systeem in acht worden genomen). (Groep-TT, Groep-Eind)', linked_project: 'Uitvoerings', feedback: 'Good teamwork, but some areas need improvement.', beoordeling: 'Voldoende' },
              { id: 3, project: 'WoR-P-3', verantwoording: 'Bewaakt de bruikbaarheid en leesbaarheid van de onderzoeksdocumenten rekening houdend met relevantie en concrete bijdrage aan het project en het hanteren van een gestructureerde aanpak en gebruik van onderzoekstechnieken. (Groep-TT, Groep-Eind)\nLevert een kwalitatief en kwantitatief representatieve bijdrage met voldoende complexiteit aan de onderzoeksdocumentatie. (Ind-TT, Ind-Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 4, project: 'WoR-P-4', verantwoording: 'Bewaakt actief de bruikbaarheid en leesbaarheid ontwerpdocumenten rekening houdend met aansluiting op de analysedocumentatie en (technische) bruikbaarheid voor implementatie. (Groep-TT, Groep-Eind)\nLevert een kwalitatief en kwantitatief representatieve bijdrage met voldoende complexiteit aan de ontwerpdocumentatie. (Ind-TT, Ind-Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 5, project: 'WoR-P-5', verantwoording: 'Gebruikt op professionele en adequate wijze de relevante collaborative tools. (Ind-TT, Ind-Eind)\nZet collaborative tools in voor beheersen van het groepsproces en het bewaken van de geleverde productkwaliteit. (Groep-TT, Groep-Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 6, project: 'WoR-P-6', verantwoording: 'Bewaakt de voortgang en bruikbaarheid van de implementatie (code) en bijbehorende code-documentatie en verifieert of deze in lijn is (en blijft) met de ontwerp-documentatie. (Groep-TT, Groep-Eind)\nEvalueert de analyse, ontwerp, realisatie en testen van het systeem en beoordeelt de traceability en eenheid ervan. (Ind-TT, Ind-Eind)\nLevert een kwalitatief en kwantitatief representatieve bijdrage met voldoende complexiteit aan de implementatie van het systeem. (Ind-TT, Ind-Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' },
              { id: 7, project: 'WoR-P-7', verantwoording: 'Bewaakt de kwaliteit van de ontwikkelde software rekening houdend met de aanwezige onderbouwing bij het ontwerp en de daarin gebruikte design-patterns, maar ook de resultaten van code-analysetools. (Groep-TT, Groep-Eind)\nLevert een kwalitatief en kwantitatief representatieve bijdrage aan de kwaliteit van de software en de eventuele verbetering ervan door het gebruik van design patterns en refactoringtechnieken. (Ind-TT, Ind-Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 8, project: 'WoR-P-8', verantwoording: 'Bewaakt de werking van het gerealiseerde systeem (en onderliggende deelsystemen) op basis van de test-coverage en test-resultaten, waarbij ook wordt gecontroleerd of de testen aansluiten bij de ontwerpdocumentatie. (Groep-TT, Groep-Eind)\nLevert een kwalitatief en kwantitatief representatieve bijdrage met voldoende complexiteit aan de testdocumentatie. (Ind-TT, Ind-Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 9, project: 'WoR-P-9', verantwoording: 'Analyseert de samenwerking in de groep, stuurt en analyseert interventies, om een groepsopdracht tot een goed einde te brengen. (Groep-TT, Groep-Eind)\nVoert zijn rollen zoals deze in RUP en/of Scrum beschreven staat correct uit. (Ind-TT, Ind-Eind)\nWerkt tijdens het gehele project samen met de leden van het team en stuurt zo nodig actief bij om het functioneren van de groep en de individuen daarbinnen te verbeteren. (Ind-TT, Ind-Eind)\nWerkt volgens afspraken die gemaakt zijn in het plan van aanpak. (Ind-TT, Ind-Eind)', linked_project: 'Voorbereiding', feedback: 'Good effort, but more persistence needed.', beoordeling: 'Onvoldoende' },
              { id: 10, project: 'WoR-P-10', verantwoording: 'Deelt gedurende het proces opgedane nieuwe kennis en vaardigheden met de groep. (Groep-TT, Groep-Eind)\nStuurt bewust in zijn eigen beroepsmatige ontwikkeling, formuleert hier vragen bij en reflecteert op zijn ontwikkeling gedurende het proces. (Ind-TT, Ind-Eind)\nVerwerft tijdens het proces waar nodig nieuwe kennis en vaardigheden en ontwikkelt zich tot deskundige op het onderwerp van de projectopdracht. (Ind-TT, Ind-Eind)', linked_project: 'Afronding', feedback: 'Needs more understanding of key concepts.', beoordeling: 'Onvoldoende' }
            ]
          },
        ]
      },
    ]
  }
]);

console.log("DATA", data.value[0].werkproces[1].criteria[1].criteria)

const sortKey = ref('');
const sortOrder = ref('asc');
const selectedFase = ref('');
const selectedBeoordeling = ref('');
const currentPage = ref(1);
const itemsPerPage = 10;



const sort = (key) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortKey.value = key;
    sortOrder.value = 'asc';
  }
};

const getSortIcon = (key) => {
  if (sortKey.value !== key) return 'bi bi-arrow-down-up';
  return sortOrder.value === 'asc' ? 'bi bi-arrow-up' : 'bi bi-arrow-down';
};

const sortedData = computed(() => {
  // console.log(data.value[0].werkproces);
  const sorted = [...data.value[0].werkproces[currentWerkProces.value].criteria];
  if (sortKey.value) {
    sorted.sort((a, b) => {
      const aValue = a[sortKey.value];
      const bValue = b[sortKey.value];

      if (typeof aValue === 'string') {
        return sortOrder.value === 'asc'
          ? aValue.localeCompare(bValue)
          : bValue.localeCompare(aValue);
      } else {
        return sortOrder.value === 'asc'
          ? aValue - bValue
          : bValue - aValue;
      }
    });
  }
  return sorted;
});


const filterData = () => {
  currentPage.value = 1;
};

const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

const getRowClass = (beoordeling) => {
  switch (beoordeling) {
    case 'Goed':
      return 'table-success';
    case 'Voldoende':
      return 'table-info';
    case 'Onvoldoende':
      return 'table-danger';
  }
};

const completionCriteriaTracker = (criteria) => {
  return criteria.reduce((completed, item) => 
    (item.beoordeling === 'Goed' || item.beoordeling === 'Voldoende') ? completed + 1 : completed, 0);
};

const getBadgeClass = (completion) => {
  completion *= 100;
  if (completion == 100) {
    return 'badge bg-success';
  } else if (completion >= 60) {
    return 'badge bg-info';
  } else if (completion >= 40) {
    return 'badge bg-warning';
  } else {
    return 'badge bg-danger';
  }
};

// Make currentWerkProces reactive
const currentWerkProces = ref(0); // Wrap currentWerkProces in a ref

const incrementWerkProces = () => {
  currentWerkProces.value += 1;
  // console.log(currentWerkProces.value);
};

const uniqueFases = computed(() => {
  // console.log(data.value[0].werkproces[currentWerkProces.value].criteria)
  const fases = data.value[0].werkproces[currentWerkProces.value].criteria.map(item => item.fase);
  return [...new Set(fases)];
});

const uniqueBeoordelingen = computed(() => {
  const beoordelingen = data.value[0].werkproces[currentWerkProces.value].criteria.map(item => item.beoordeling);
  return [...new Set(beoordelingen)];
});

const filteredData = computed(() => {
  let filtered = sortedData.value;
  if (selectedFase.value) {
    filtered = filtered.filter(item => item.fase === selectedFase.value);
  }
  if (selectedBeoordeling.value) {
    filtered = filtered.filter(item => item.beoordeling === selectedBeoordeling.value);
  }
  return filtered;
});

const totalPages = computed(() => Math.ceil(filteredData.value.length / itemsPerPage));

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return filteredData.value.slice(start, end);
});

// Calculate percentages
const percentageCompleted = computed(() => {
  const completed = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Goed' || item.beoordeling === 'Voldoende').length;
  return ((completed / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

const percentageVoldoende = computed(() => {
  const voldoende = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Voldoende').length;
  return ((voldoende / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

const percentageGoed = computed(() => {
  const goed = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Goed').length;
  return ((goed / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

const percentageOnvoldoende = computed(() => {
  const onvoldoende = data.value[0].werkproces[currentWerkProces.value].criteria.filter(item => item.beoordeling === 'Onvoldoende').length;
  return ((onvoldoende / data.value[0].werkproces[currentWerkProces.value].criteria.length) * 100).toFixed(2);
});

// Donut chart styles
const getDonutStyle = (percentage, color) => {
  const rotation = (percentage / 100) * 360;
  return {
    background: `conic-gradient(${color} ${rotation}deg, #e9ecef ${rotation}deg)`,
  };
};
</script>

<style>
.chart-container {
  width: 100px;
  height: 100px;
  position: relative;
}

.donut-chart {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: conic-gradient(#007bff 0deg, #e9ecef 0deg);
}

.chart-label {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-weight: bold;
}
</style>
