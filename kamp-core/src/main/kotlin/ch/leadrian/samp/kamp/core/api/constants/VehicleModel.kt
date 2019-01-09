package ch.leadrian.samp.kamp.core.api.constants

import ch.leadrian.samp.kamp.core.TextKeys
import ch.leadrian.samp.kamp.core.api.text.HasTextKey
import ch.leadrian.samp.kamp.core.api.text.TextKey
import org.apache.commons.collections4.trie.PatriciaTrie

/**
 * Taken from https://github.com/Shoebill/shoebill-api
 */
enum class VehicleModel(
        override val value: Int,
        override val textKey: TextKey,
        val modelName: String,
        val type: VehicleType,
        val numberOfSeats: Int
) : ConstantValue<Int>, HasTextKey {
    LANDSTALKER(SAMPConstants.VEHICLE_LANDSTALKER, TextKeys.vehicle.model.name.landstalker, "Landstalker", VehicleType.CAR, 4),
    BRAVURA(SAMPConstants.VEHICLE_BRAVURA, TextKeys.vehicle.model.name.bravura, "Bravura", VehicleType.CAR, 2),
    BUFFALO(SAMPConstants.VEHICLE_BUFFALO, TextKeys.vehicle.model.name.buffalo, "Buffalo", VehicleType.CAR, 2),
    LINERUNNER(SAMPConstants.VEHICLE_LINERUNNER, TextKeys.vehicle.model.name.linerunner, "Linerunner", VehicleType.CAR, 2),
    PERRENIAL(SAMPConstants.VEHICLE_PERRENIAL, TextKeys.vehicle.model.name.perrenial, "Perennial", VehicleType.CAR, 4),
    SENTINEL(SAMPConstants.VEHICLE_SENTINEL, TextKeys.vehicle.model.name.sentinel, "Sentinel", VehicleType.CAR, 4),
    DUMPER(SAMPConstants.VEHICLE_DUMPER, TextKeys.vehicle.model.name.dumper, "Dumper", VehicleType.CAR, 1),
    FIRETRUCK(SAMPConstants.VEHICLE_FIRETRUCK, TextKeys.vehicle.model.name.firetruck, "Firetruck", VehicleType.CAR, 2),
    TRASHMASTER(SAMPConstants.VEHICLE_TRASHMASTER, TextKeys.vehicle.model.name.trashmaster, "Trashmaster", VehicleType.CAR, 2),
    STRETCH(SAMPConstants.VEHICLE_STRETCH, TextKeys.vehicle.model.name.stretch, "Stretch", VehicleType.CAR, 4),
    MANANA(SAMPConstants.VEHICLE_MANANA, TextKeys.vehicle.model.name.manana, "Manana", VehicleType.CAR, 2),
    INFERNUS(SAMPConstants.VEHICLE_INFERNUS, TextKeys.vehicle.model.name.infernus, "Infernus", VehicleType.CAR, 2),
    VOODOO(SAMPConstants.VEHICLE_VOODOO, TextKeys.vehicle.model.name.voodoo, "Voodoo", VehicleType.CAR, 2),
    PONY(SAMPConstants.VEHICLE_PONY, TextKeys.vehicle.model.name.pony, "Pony", VehicleType.CAR, 2),
    MULE(SAMPConstants.VEHICLE_MULE, TextKeys.vehicle.model.name.mule, "Mule", VehicleType.CAR, 2),
    CHEETAH(SAMPConstants.VEHICLE_CHEETAH, TextKeys.vehicle.model.name.cheetah, "Cheetah", VehicleType.CAR, 2),
    AMBULANCE(SAMPConstants.VEHICLE_AMBULANCE, TextKeys.vehicle.model.name.ambulance, "Ambulance", VehicleType.CAR, 4),
    LEVIATHAN(SAMPConstants.VEHICLE_LEVIATHAN, TextKeys.vehicle.model.name.leviathan, "Leviathan", VehicleType.HELICOPTER, 2),
    MOONBEAM(SAMPConstants.VEHICLE_MOONBEAM, TextKeys.vehicle.model.name.moonbeam, "Moonbeam", VehicleType.CAR, 4),
    ESPERANTO(SAMPConstants.VEHICLE_ESPERANTO, TextKeys.vehicle.model.name.esperanto, "Esperanto", VehicleType.CAR, 2),
    TAXI(SAMPConstants.VEHICLE_TAXI, TextKeys.vehicle.model.name.taxi, "Taxi", VehicleType.CAR, 4),
    WASHINGTON(SAMPConstants.VEHICLE_WASHINGTON, TextKeys.vehicle.model.name.washington, "Washington", VehicleType.CAR, 4),
    BOBCAT(SAMPConstants.VEHICLE_BOBCAT, TextKeys.vehicle.model.name.bobcat, "Bobcat", VehicleType.CAR, 2),
    MRWHOOPEE(SAMPConstants.VEHICLE_MRWHOOPEE, TextKeys.vehicle.model.name.mrwhoopee, "Mr Whoopee", VehicleType.CAR, 2),
    BFINJECTION(SAMPConstants.VEHICLE_BFINJECTION, TextKeys.vehicle.model.name.bfinjection, "BF Injection", VehicleType.CAR, 2),
    HUNTER(SAMPConstants.VEHICLE_HUNTER, TextKeys.vehicle.model.name.hunter, "Hunter", VehicleType.HELICOPTER, 1),
    PREMIER(SAMPConstants.VEHICLE_PREMIER, TextKeys.vehicle.model.name.premier, "Premier", VehicleType.CAR, 4),
    ENFORCER(SAMPConstants.VEHICLE_ENFORCER, TextKeys.vehicle.model.name.enforcer, "Enforcer", VehicleType.CAR, 4),
    SECURICAR(SAMPConstants.VEHICLE_SECURICAR, TextKeys.vehicle.model.name.securicar, "Securicar", VehicleType.CAR, 4),
    BANSHEE(SAMPConstants.VEHICLE_BANSHEE, TextKeys.vehicle.model.name.banshee, "Banshee", VehicleType.CAR, 2),
    PREDATOR(SAMPConstants.VEHICLE_PREDATOR, TextKeys.vehicle.model.name.predator, "Predator", VehicleType.BOAT, 0),
    BUS(SAMPConstants.VEHICLE_BUS, TextKeys.vehicle.model.name.bus, "Bus", VehicleType.CAR, 8),
    RHINO(SAMPConstants.VEHICLE_RHINO, TextKeys.vehicle.model.name.rhino, "Rhino", VehicleType.TANK, 1),
    BARRACKS(SAMPConstants.VEHICLE_BARRACKS, TextKeys.vehicle.model.name.barracks, "Barracks", VehicleType.CAR, 2),
    HOTKNIFE(SAMPConstants.VEHICLE_HOTKNIFE, TextKeys.vehicle.model.name.hotknife, "Hotknife", VehicleType.CAR, 2),
    ARTICLETRAILER1(SAMPConstants.VEHICLE_ARTICLETRAILER1, TextKeys.vehicle.model.name.articletrailer1, "Trailer", VehicleType.TRAILER, 0),
    PREVION(SAMPConstants.VEHICLE_PREVION, TextKeys.vehicle.model.name.previon, "Previon", VehicleType.CAR, 2),
    COACH(SAMPConstants.VEHICLE_COACH, TextKeys.vehicle.model.name.coach, "Coach", VehicleType.CAR, 8),
    CABBIE(SAMPConstants.VEHICLE_CABBIE, TextKeys.vehicle.model.name.cabbie, "Cabbie", VehicleType.CAR, 4),
    STALLION(SAMPConstants.VEHICLE_STALLION, TextKeys.vehicle.model.name.stallion, "Stallion", VehicleType.CAR, 2),
    RUMPO(SAMPConstants.VEHICLE_RUMPO, TextKeys.vehicle.model.name.rumpo, "Rumpo", VehicleType.CAR, 4),
    RCBANDIT(SAMPConstants.VEHICLE_RCBANDIT, TextKeys.vehicle.model.name.rcbandit, "RC Bandit", VehicleType.REMOTE_CONTROL, 1),
    ROMERO(SAMPConstants.VEHICLE_ROMERO, TextKeys.vehicle.model.name.romero, "Romero", VehicleType.CAR, 2),
    PACKER(SAMPConstants.VEHICLE_PACKER, TextKeys.vehicle.model.name.packer, "Packer", VehicleType.CAR, 2),
    MONSTER(SAMPConstants.VEHICLE_MONSTER, TextKeys.vehicle.model.name.monster, "Monster", VehicleType.CAR, 2),
    ADMIRAL(SAMPConstants.VEHICLE_ADMIRAL, TextKeys.vehicle.model.name.admiral, "Admiral", VehicleType.CAR, 4),
    SQUALO(SAMPConstants.VEHICLE_SQUALO, TextKeys.vehicle.model.name.squalo, "Squalo", VehicleType.BOAT, 0),
    SEASPARROW(SAMPConstants.VEHICLE_SEASPARROW, TextKeys.vehicle.model.name.seasparrow, "Seasparrow", VehicleType.HELICOPTER, 2),
    PIZZABOY(SAMPConstants.VEHICLE_PIZZABOY, TextKeys.vehicle.model.name.pizzaboy, "Pizzaboy", VehicleType.MOTORBIKE, 1),
    TRAM(SAMPConstants.VEHICLE_TRAM, TextKeys.vehicle.model.name.tram, "Tram", VehicleType.TRAIN, 4),
    ARTICLETRAILER2(SAMPConstants.VEHICLE_ARTICLETRAILER2, TextKeys.vehicle.model.name.articletrailer2, "Trailer", VehicleType.TRAILER, 0),
    TURISMO(SAMPConstants.VEHICLE_TURISMO, TextKeys.vehicle.model.name.turismo, "Turismo", VehicleType.CAR, 2),
    SPEEDER(SAMPConstants.VEHICLE_SPEEDER, TextKeys.vehicle.model.name.speeder, "Speeder", VehicleType.BOAT, 0),
    REEFER(SAMPConstants.VEHICLE_REEFER, TextKeys.vehicle.model.name.reefer, "Reefer", VehicleType.BOAT, 0),
    TROPIC(SAMPConstants.VEHICLE_TROPIC, TextKeys.vehicle.model.name.tropic, "Tropic", VehicleType.BOAT, 0),
    FLATBED(SAMPConstants.VEHICLE_FLATBED, TextKeys.vehicle.model.name.flatbed, "Flatbed", VehicleType.CAR, 2),
    YANKEE(SAMPConstants.VEHICLE_YANKEE, TextKeys.vehicle.model.name.yankee, "Yankee", VehicleType.CAR, 2),
    CADDY(SAMPConstants.VEHICLE_CADDY, TextKeys.vehicle.model.name.caddy, "Caddy", VehicleType.CAR, 2),
    SOLAIR(SAMPConstants.VEHICLE_SOLAIR, TextKeys.vehicle.model.name.solair, "Solair", VehicleType.CAR, 4),
    BERKLEYSRCVAN(SAMPConstants.VEHICLE_BERKLEYSRCVAN, TextKeys.vehicle.model.name.berkleysrcvan, "Berkleys RC Van", VehicleType.CAR, 4),
    SKIMMER(SAMPConstants.VEHICLE_SKIMMER, TextKeys.vehicle.model.name.skimmer, "Skimmer", VehicleType.AIRCRAFT, 2),
    PCJ600(SAMPConstants.VEHICLE_PCJ600, TextKeys.vehicle.model.name.pcj600, "PCJ-600", VehicleType.MOTORBIKE, 2),
    FAGGIO(SAMPConstants.VEHICLE_FAGGIO, TextKeys.vehicle.model.name.faggio, "Faggio", VehicleType.MOTORBIKE, 2),
    FREEWAY(SAMPConstants.VEHICLE_FREEWAY, TextKeys.vehicle.model.name.freeway, "Freeway", VehicleType.MOTORBIKE, 0),
    RCBARON(SAMPConstants.VEHICLE_RCBARON, TextKeys.vehicle.model.name.rcbaron, "RC Baron", VehicleType.REMOTE_CONTROL, 1),
    RCRAIDER(SAMPConstants.VEHICLE_RCRAIDER, TextKeys.vehicle.model.name.rcraider, "RC Raider", VehicleType.REMOTE_CONTROL, 1),
    GLENDALE(SAMPConstants.VEHICLE_GLENDALE, TextKeys.vehicle.model.name.glendale, "Glendale", VehicleType.CAR, 4),
    OCEANIC(SAMPConstants.VEHICLE_OCEANIC, TextKeys.vehicle.model.name.oceanic, "Oceanic", VehicleType.CAR, 4),
    SANCHEZ(SAMPConstants.VEHICLE_SANCHEZ, TextKeys.vehicle.model.name.sanchez, "Sanchez", VehicleType.MOTORBIKE, 2),
    SPARROW(SAMPConstants.VEHICLE_SPARROW, TextKeys.vehicle.model.name.sparrow, "Sparrow", VehicleType.HELICOPTER, 2),
    PATRIOT(SAMPConstants.VEHICLE_PATRIOT, TextKeys.vehicle.model.name.patriot, "Patriot", VehicleType.CAR, 4),
    QUAD(SAMPConstants.VEHICLE_QUAD, TextKeys.vehicle.model.name.quad, "Quad", VehicleType.MOTORBIKE, 2),
    COASTGUARD(SAMPConstants.VEHICLE_COASTGUARD, TextKeys.vehicle.model.name.coastguard, "Coastguard", VehicleType.BOAT, 0),
    DINGHY(SAMPConstants.VEHICLE_DINGHY, TextKeys.vehicle.model.name.dinghy, "Dinghy", VehicleType.BOAT, 0),
    HERMES(SAMPConstants.VEHICLE_HERMES, TextKeys.vehicle.model.name.hermes, "Hermes", VehicleType.CAR, 2),
    SABRE(SAMPConstants.VEHICLE_SABRE, TextKeys.vehicle.model.name.sabre, "Sabre", VehicleType.CAR, 2),
    RUSTLER(SAMPConstants.VEHICLE_RUSTLER, TextKeys.vehicle.model.name.rustler, "Rustler", VehicleType.AIRCRAFT, 1),
    ZR350(SAMPConstants.VEHICLE_ZR350, TextKeys.vehicle.model.name.zr350, "ZR-350", VehicleType.CAR, 2),
    WALTON(SAMPConstants.VEHICLE_WALTON, TextKeys.vehicle.model.name.walton, "Walton", VehicleType.CAR, 2),
    REGINA(SAMPConstants.VEHICLE_REGINA, TextKeys.vehicle.model.name.regina, "Regina", VehicleType.CAR, 4),
    COMET(SAMPConstants.VEHICLE_COMET, TextKeys.vehicle.model.name.comet, "Comet", VehicleType.CAR, 2),
    BMX(SAMPConstants.VEHICLE_BMX, TextKeys.vehicle.model.name.bmx, "BMX", VehicleType.BICYCLE, 1),
    BURRITO(SAMPConstants.VEHICLE_BURRITO, TextKeys.vehicle.model.name.burrito, "Burrito", VehicleType.CAR, 4),
    CAMPER(SAMPConstants.VEHICLE_CAMPER, TextKeys.vehicle.model.name.camper, "Camper", VehicleType.CAR, 3),
    MARQUIS(SAMPConstants.VEHICLE_MARQUIS, TextKeys.vehicle.model.name.marquis, "Marquis", VehicleType.BOAT, 0),
    BAGGAGE(SAMPConstants.VEHICLE_BAGGAGE, TextKeys.vehicle.model.name.baggage, "Baggage", VehicleType.CAR, 1),
    DOZER(SAMPConstants.VEHICLE_DOZER, TextKeys.vehicle.model.name.dozer, "Dozer", VehicleType.CAR, 1),
    MAVERICK(SAMPConstants.VEHICLE_MAVERICK, TextKeys.vehicle.model.name.maverick, "Maverick", VehicleType.HELICOPTER, 4),
    SANNEWSMAVERICK(SAMPConstants.VEHICLE_SANNEWSMAVERICK, TextKeys.vehicle.model.name.sannewsmaverick, "News Chopper", VehicleType.HELICOPTER, 2),
    RANCHER(SAMPConstants.VEHICLE_RANCHER, TextKeys.vehicle.model.name.rancher, "Rancher", VehicleType.CAR, 2),
    FBIRANCHER(SAMPConstants.VEHICLE_FBIRANCHER, TextKeys.vehicle.model.name.fbirancher, "FBI Rancher", VehicleType.CAR, 4),
    VIRGO(SAMPConstants.VEHICLE_VIRGO, TextKeys.vehicle.model.name.virgo, "Virgo", VehicleType.CAR, 2),
    GREENWOOD(SAMPConstants.VEHICLE_GREENWOOD, TextKeys.vehicle.model.name.greenwood, "Greenwood", VehicleType.CAR, 2),
    JETMAX(SAMPConstants.VEHICLE_JETMAX, TextKeys.vehicle.model.name.jetmax, "Jetmax", VehicleType.BOAT, 0),
    HOTRINGRACER(SAMPConstants.VEHICLE_HOTRINGRACER, TextKeys.vehicle.model.name.hotringracer, "Hotring", VehicleType.CAR, 2),
    SANDKING(SAMPConstants.VEHICLE_SANDKING, TextKeys.vehicle.model.name.sandking, "Sandking", VehicleType.CAR, 2),
    BLISTACOMPACT(SAMPConstants.VEHICLE_BLISTACOMPACT, TextKeys.vehicle.model.name.blistacompact, "Blista Compact", VehicleType.CAR, 2),
    POLICEMAVERICK(SAMPConstants.VEHICLE_POLICEMAVERICK, TextKeys.vehicle.model.name.policemaverick, "Police Maverick", VehicleType.HELICOPTER, 4),
    BOXVILLE(SAMPConstants.VEHICLE_BOXVILLE, TextKeys.vehicle.model.name.boxville, "Boxville", VehicleType.CAR, 4),
    BENSON(SAMPConstants.VEHICLE_BENSON, TextKeys.vehicle.model.name.benson, "Benson", VehicleType.CAR, 2),
    MESA(SAMPConstants.VEHICLE_MESA, TextKeys.vehicle.model.name.mesa, "Mesa", VehicleType.CAR, 2),
    RCGOBLIN(SAMPConstants.VEHICLE_RCGOBLIN, TextKeys.vehicle.model.name.rcgoblin, "RC Goblin", VehicleType.REMOTE_CONTROL, 1),
    HOTRINGRACERA(SAMPConstants.VEHICLE_HOTRINGRACERA, TextKeys.vehicle.model.name.hotringracera, "Hotring Racer", VehicleType.CAR, 2),
    HOTRINGRACERB(SAMPConstants.VEHICLE_HOTRINGRACERB, TextKeys.vehicle.model.name.hotringracerb, "Hotring Racer", VehicleType.CAR, 2),
    BLOODRINGBANGER(SAMPConstants.VEHICLE_BLOODRINGBANGER, TextKeys.vehicle.model.name.bloodringbanger, "Bloodring Banger", VehicleType.CAR, 2),
    RANCHERLURE(SAMPConstants.VEHICLE_RANCHERLURE, TextKeys.vehicle.model.name.rancherlure, "Rancher", VehicleType.CAR, 2),
    SUPERGT(SAMPConstants.VEHICLE_SUPERGT, TextKeys.vehicle.model.name.supergt, "Super GT", VehicleType.CAR, 2),
    ELEGANT(SAMPConstants.VEHICLE_ELEGANT, TextKeys.vehicle.model.name.elegant, "Elegant", VehicleType.CAR, 4),
    JOURNEY(SAMPConstants.VEHICLE_JOURNEY, TextKeys.vehicle.model.name.journey, "Journey", VehicleType.CAR, 2),
    BIKE(SAMPConstants.VEHICLE_BIKE, TextKeys.vehicle.model.name.bike, "Bike", VehicleType.BICYCLE, 1),
    MOUNTAINBIKE(SAMPConstants.VEHICLE_MOUNTAINBIKE, TextKeys.vehicle.model.name.mountainbike, "Mountain Bike", VehicleType.BICYCLE, 1),
    BEAGLE(SAMPConstants.VEHICLE_BEAGLE, TextKeys.vehicle.model.name.beagle, "Beagle", VehicleType.AIRCRAFT, 2),
    CROPDUST(SAMPConstants.VEHICLE_CROPDUST, TextKeys.vehicle.model.name.cropdust, "Cropdust", VehicleType.AIRCRAFT, 1),
    STUNTPLANE(SAMPConstants.VEHICLE_STUNTPLANE, TextKeys.vehicle.model.name.stuntplane, "Stunt", VehicleType.AIRCRAFT, 1),
    TANKER(SAMPConstants.VEHICLE_TANKER, TextKeys.vehicle.model.name.tanker, "Tanker", VehicleType.CAR, 2),
    ROADTRAIN(SAMPConstants.VEHICLE_ROADTRAIN, TextKeys.vehicle.model.name.roadtrain, "RoadTrain", VehicleType.CAR, 2),
    NEBULA(SAMPConstants.VEHICLE_NEBULA, TextKeys.vehicle.model.name.nebula, "Nebula", VehicleType.CAR, 4),
    MAJESTIC(SAMPConstants.VEHICLE_MAJESTIC, TextKeys.vehicle.model.name.majestic, "Majestic", VehicleType.CAR, 2),
    BUCCANEER(SAMPConstants.VEHICLE_BUCCANEER, TextKeys.vehicle.model.name.buccaneer, "Buccaneer", VehicleType.CAR, 2),
    SHAMAL(SAMPConstants.VEHICLE_SHAMAL, TextKeys.vehicle.model.name.shamal, "Shamal", VehicleType.AIRCRAFT, 1),
    HYDRA(SAMPConstants.VEHICLE_HYDRA, TextKeys.vehicle.model.name.hydra, "Hydra", VehicleType.AIRCRAFT, 1),
    FCR900(SAMPConstants.VEHICLE_FCR900, TextKeys.vehicle.model.name.fcr900, "FCR-900", VehicleType.MOTORBIKE, 2),
    NRG500(SAMPConstants.VEHICLE_NRG500, TextKeys.vehicle.model.name.nrg500, "NRG-500", VehicleType.MOTORBIKE, 2),
    HPV1000(SAMPConstants.VEHICLE_HPV1000, TextKeys.vehicle.model.name.hpv1000, "HPV1000", VehicleType.MOTORBIKE, 2),
    CEMENTTRUCK(SAMPConstants.VEHICLE_CEMENTTRUCK, TextKeys.vehicle.model.name.cementtruck, "Cement Truck", VehicleType.CAR, 2),
    TOWTRUCK(SAMPConstants.VEHICLE_TOWTRUCK, TextKeys.vehicle.model.name.towtruck, "Tow Truck", VehicleType.CAR, 2),
    FORTUNE(SAMPConstants.VEHICLE_FORTUNE, TextKeys.vehicle.model.name.fortune, "Fortune", VehicleType.CAR, 2),
    CADRONA(SAMPConstants.VEHICLE_CADRONA, TextKeys.vehicle.model.name.cadrona, "Cadrona", VehicleType.CAR, 2),
    FBITRUCK(SAMPConstants.VEHICLE_FBITRUCK, TextKeys.vehicle.model.name.fbitruck, "FBI Truck", VehicleType.CAR, 2),
    WILLARD(SAMPConstants.VEHICLE_WILLARD, TextKeys.vehicle.model.name.willard, "Willard", VehicleType.CAR, 4),
    FORKLIFT(SAMPConstants.VEHICLE_FORKLIFT, TextKeys.vehicle.model.name.forklift, "Forklift", VehicleType.CAR, 1),
    TRACTOR(SAMPConstants.VEHICLE_TRACTOR, TextKeys.vehicle.model.name.tractor, "Tractor", VehicleType.CAR, 1),
    COMBINE(SAMPConstants.VEHICLE_COMBINE, TextKeys.vehicle.model.name.combine, "Combine", VehicleType.CAR, 1),
    FELTZER(SAMPConstants.VEHICLE_FELTZER, TextKeys.vehicle.model.name.feltzer, "Feltzer", VehicleType.CAR, 2),
    REMINGTON(SAMPConstants.VEHICLE_REMINGTON, TextKeys.vehicle.model.name.remington, "Remington", VehicleType.CAR, 2),
    SLAMVAN(SAMPConstants.VEHICLE_SLAMVAN, TextKeys.vehicle.model.name.slamvan, "Slamvan", VehicleType.CAR, 2),
    BLADE(SAMPConstants.VEHICLE_BLADE, TextKeys.vehicle.model.name.blade, "Blade", VehicleType.CAR, 2),
    FREIGHT(SAMPConstants.VEHICLE_FREIGHT, TextKeys.vehicle.model.name.freight, "Freight", VehicleType.TRAIN, 2),
    BROWNSTREAK(SAMPConstants.VEHICLE_BROWNSTREAK, TextKeys.vehicle.model.name.brownstreak, "Streak", VehicleType.TRAIN, 2),
    VORTEX(SAMPConstants.VEHICLE_VORTEX, TextKeys.vehicle.model.name.vortex, "Vortex", VehicleType.BOAT, 0),
    VINCENT(SAMPConstants.VEHICLE_VINCENT, TextKeys.vehicle.model.name.vincent, "Vincent", VehicleType.CAR, 4),
    BULLET(SAMPConstants.VEHICLE_BULLET, TextKeys.vehicle.model.name.bullet, "Bullet", VehicleType.CAR, 2),
    CLOVER(SAMPConstants.VEHICLE_CLOVER, TextKeys.vehicle.model.name.clover, "Clover", VehicleType.CAR, 2),
    SADLER(SAMPConstants.VEHICLE_SADLER, TextKeys.vehicle.model.name.sadler, "Sadler", VehicleType.CAR, 2),
    FIRETRUCKLA(SAMPConstants.VEHICLE_FIRETRUCKLA, TextKeys.vehicle.model.name.firetruckla, "Firetruck", VehicleType.CAR, 2),
    HUSTLER(SAMPConstants.VEHICLE_HUSTLER, TextKeys.vehicle.model.name.hustler, "Hustler", VehicleType.CAR, 2),
    INTRUDER(SAMPConstants.VEHICLE_INTRUDER, TextKeys.vehicle.model.name.intruder, "Intruder", VehicleType.CAR, 4),
    PRIMO(SAMPConstants.VEHICLE_PRIMO, TextKeys.vehicle.model.name.primo, "Primo", VehicleType.CAR, 4),
    CARGOBOB(SAMPConstants.VEHICLE_CARGOBOB, TextKeys.vehicle.model.name.cargobob, "Cargobob", VehicleType.HELICOPTER, 2),
    TAMPA(SAMPConstants.VEHICLE_TAMPA, TextKeys.vehicle.model.name.tampa, "Tampa", VehicleType.CAR, 2),
    SUNRISE(SAMPConstants.VEHICLE_SUNRISE, TextKeys.vehicle.model.name.sunrise, "Sunrise", VehicleType.CAR, 4),
    MERIT(SAMPConstants.VEHICLE_MERIT, TextKeys.vehicle.model.name.merit, "Merit", VehicleType.CAR, 4),
    UTILITYVAN(SAMPConstants.VEHICLE_UTILITYVAN, TextKeys.vehicle.model.name.utilityvan, "Utility", VehicleType.CAR, 2),
    NEVADA(SAMPConstants.VEHICLE_NEVADA, TextKeys.vehicle.model.name.nevada, "Nevada", VehicleType.AIRCRAFT, 1),
    YOSEMITE(SAMPConstants.VEHICLE_YOSEMITE, TextKeys.vehicle.model.name.yosemite, "Yosemite", VehicleType.CAR, 2),
    WINDSOR(SAMPConstants.VEHICLE_WINDSOR, TextKeys.vehicle.model.name.windsor, "Windsor", VehicleType.CAR, 2),
    MONSTERA(SAMPConstants.VEHICLE_MONSTERA, TextKeys.vehicle.model.name.monstera, "Monster", VehicleType.CAR, 2),
    MONSTERB(SAMPConstants.VEHICLE_MONSTERB, TextKeys.vehicle.model.name.monsterb, "Monster", VehicleType.CAR, 2),
    URANUS(SAMPConstants.VEHICLE_URANUS, TextKeys.vehicle.model.name.uranus, "Uranus", VehicleType.CAR, 2),
    JESTER(SAMPConstants.VEHICLE_JESTER, TextKeys.vehicle.model.name.jester, "Jester", VehicleType.CAR, 2),
    SULTAN(SAMPConstants.VEHICLE_SULTAN, TextKeys.vehicle.model.name.sultan, "Sultan", VehicleType.CAR, 4),
    STRATUM(SAMPConstants.VEHICLE_STRATUM, TextKeys.vehicle.model.name.stratum, "Stratum", VehicleType.CAR, 4),
    ELEGY(SAMPConstants.VEHICLE_ELEGY, TextKeys.vehicle.model.name.elegy, "Elegy", VehicleType.CAR, 2),
    RAINDANCE(SAMPConstants.VEHICLE_RAINDANCE, TextKeys.vehicle.model.name.raindance, "Raindance", VehicleType.HELICOPTER, 2),
    RCTIGER(SAMPConstants.VEHICLE_RCTIGER, TextKeys.vehicle.model.name.rctiger, "RC Tiger", VehicleType.REMOTE_CONTROL, 1),
    FLASH(SAMPConstants.VEHICLE_FLASH, TextKeys.vehicle.model.name.flash, "Flash", VehicleType.CAR, 2),
    TAHOMA(SAMPConstants.VEHICLE_TAHOMA, TextKeys.vehicle.model.name.tahoma, "Tahoma", VehicleType.CAR, 4),
    SAVANNA(SAMPConstants.VEHICLE_SAVANNA, TextKeys.vehicle.model.name.savanna, "Savanna", VehicleType.CAR, 4),
    BANDITO(SAMPConstants.VEHICLE_BANDITO, TextKeys.vehicle.model.name.bandito, "Bandito", VehicleType.CAR, 1),
    FREIGHTFLATTRAILER(SAMPConstants.VEHICLE_FREIGHTFLATTRAILER, TextKeys.vehicle.model.name.freightflattrailer, "Freight", VehicleType.TRAIN, 0),
    STREAKTRAILER(SAMPConstants.VEHICLE_STREAKTRAILER, TextKeys.vehicle.model.name.streaktrailer, "Trailer", VehicleType.TRAIN, 0),
    KART(SAMPConstants.VEHICLE_KART, TextKeys.vehicle.model.name.kart, "Kart", VehicleType.CAR, 1),
    MOWER(SAMPConstants.VEHICLE_MOWER, TextKeys.vehicle.model.name.mower, "Mower", VehicleType.CAR, 1),
    DUNERIDE(SAMPConstants.VEHICLE_DUNERIDE, TextKeys.vehicle.model.name.duneride, "Duneride", VehicleType.CAR, 2),
    SWEEPER(SAMPConstants.VEHICLE_SWEEPER, TextKeys.vehicle.model.name.sweeper, "Sweeper", VehicleType.CAR, 1),
    BROADWAY(SAMPConstants.VEHICLE_BROADWAY, TextKeys.vehicle.model.name.broadway, "Broadway", VehicleType.CAR, 2),
    TORNADO(SAMPConstants.VEHICLE_TORNADO, TextKeys.vehicle.model.name.tornado, "Tornado", VehicleType.CAR, 2),
    AT400(SAMPConstants.VEHICLE_AT400, TextKeys.vehicle.model.name.at400, "AT-400", VehicleType.AIRCRAFT, 1),
    DFT30(SAMPConstants.VEHICLE_DFT30, TextKeys.vehicle.model.name.dft30, "DFT-30", VehicleType.CAR, 2),
    HUNTLEY(SAMPConstants.VEHICLE_HUNTLEY, TextKeys.vehicle.model.name.huntley, "Huntley", VehicleType.CAR, 4),
    STAFFORD(SAMPConstants.VEHICLE_STAFFORD, TextKeys.vehicle.model.name.stafford, "Stafford", VehicleType.CAR, 4),
    BF400(SAMPConstants.VEHICLE_BF400, TextKeys.vehicle.model.name.bf400, "BF-400", VehicleType.MOTORBIKE, 2),
    NEWSVAN(SAMPConstants.VEHICLE_NEWSVAN, TextKeys.vehicle.model.name.newsvan, "Newsvan", VehicleType.CAR, 2),
    TUG(SAMPConstants.VEHICLE_TUG, TextKeys.vehicle.model.name.tug, "Tug", VehicleType.CAR, 1),
    PETROLTRAILER(SAMPConstants.VEHICLE_PETROLTRAILER, TextKeys.vehicle.model.name.petroltrailer, "Trailer", VehicleType.TRAILER, 0),
    EMPEROR(SAMPConstants.VEHICLE_EMPEROR, TextKeys.vehicle.model.name.emperor, "Emperor", VehicleType.CAR, 4),
    WAYFARER(SAMPConstants.VEHICLE_WAYFARER, TextKeys.vehicle.model.name.wayfarer, "Wayfarer", VehicleType.MOTORBIKE, 2),
    EUROS(SAMPConstants.VEHICLE_EUROS, TextKeys.vehicle.model.name.euros, "Euros", VehicleType.CAR, 2),
    HOTDOG(SAMPConstants.VEHICLE_HOTDOG, TextKeys.vehicle.model.name.hotdog, "Hotdog", VehicleType.CAR, 2),
    CLUB(SAMPConstants.VEHICLE_CLUB, TextKeys.vehicle.model.name.club, "Club", VehicleType.CAR, 2),
    FREIGHTBOXTRAILER(SAMPConstants.VEHICLE_FREIGHTBOXTRAILER, TextKeys.vehicle.model.name.freightboxtrailer, "Trailer", VehicleType.TRAILER, 0),
    ARTICLETRAILER3(SAMPConstants.VEHICLE_ARTICLETRAILER3, TextKeys.vehicle.model.name.articletrailer3, "Trailer", VehicleType.TRAILER, 0),
    ANDROMADA(SAMPConstants.VEHICLE_ANDROMADA, TextKeys.vehicle.model.name.andromada, "Andromada", VehicleType.AIRCRAFT, 2),
    DODO(SAMPConstants.VEHICLE_DODO, TextKeys.vehicle.model.name.dodo, "Dodo", VehicleType.AIRCRAFT, 2),
    RCCAM(SAMPConstants.VEHICLE_RCCAM, TextKeys.vehicle.model.name.rccam, "RC Cam", VehicleType.REMOTE_CONTROL, 1),
    LAUNCH(SAMPConstants.VEHICLE_LAUNCH, TextKeys.vehicle.model.name.launch, "Launch", VehicleType.BOAT, 0),
    POLICECARLSPD(SAMPConstants.VEHICLE_POLICECARLSPD, TextKeys.vehicle.model.name.policecarlspd, "Police Car LSPD", VehicleType.CAR, 4),
    POLICECARSFPD(SAMPConstants.VEHICLE_POLICECARSFPD, TextKeys.vehicle.model.name.policecarsfpd, "Police Car SFPD", VehicleType.CAR, 4),
    POLICECARLVPD(SAMPConstants.VEHICLE_POLICECARLVPD, TextKeys.vehicle.model.name.policecarlvpd, "Police Car LVPD", VehicleType.CAR, 4),
    POLICERANGER(SAMPConstants.VEHICLE_POLICERANGER, TextKeys.vehicle.model.name.policeranger, "Police Ranger", VehicleType.CAR, 2),
    PICADOR(SAMPConstants.VEHICLE_PICADOR, TextKeys.vehicle.model.name.picador, "Picador", VehicleType.CAR, 2),
    SWAT(SAMPConstants.VEHICLE_SWAT, TextKeys.vehicle.model.name.swat, "SWAT Van", VehicleType.CAR, 2),
    ALPHA(SAMPConstants.VEHICLE_ALPHA, TextKeys.vehicle.model.name.alpha, "Alpha", VehicleType.CAR, 2),
    PHOENIX(SAMPConstants.VEHICLE_PHOENIX, TextKeys.vehicle.model.name.phoenix, "Phoenix", VehicleType.CAR, 2),
    GLENDALESHIT(SAMPConstants.VEHICLE_GLENDALESHIT, TextKeys.vehicle.model.name.glendaleshit, "Glendale", VehicleType.CAR, 4),
    SADLERSHIT(SAMPConstants.VEHICLE_SADLERSHIT, TextKeys.vehicle.model.name.sadlershit, "Sadler", VehicleType.CAR, 2),
    BAGGAGETRAILERA(SAMPConstants.VEHICLE_BAGGAGETRAILERA, TextKeys.vehicle.model.name.baggagetrailera, "Luggage Trailer", VehicleType.TRAILER, 0),
    BAGGAGETRAILERB(SAMPConstants.VEHICLE_BAGGAGETRAILERB, TextKeys.vehicle.model.name.baggagetrailerb, "Luggage Trailer", VehicleType.TRAILER, 0),
    TUGSTAIRSTRAILER(SAMPConstants.VEHICLE_TUGSTAIRSTRAILER, TextKeys.vehicle.model.name.tugstairstrailer, "Stair Trailer", VehicleType.TRAILER, 0),
    BOXBURG(SAMPConstants.VEHICLE_BOXBURG, TextKeys.vehicle.model.name.boxburg, "Boxville", VehicleType.CAR, 4),
    FARMTRAILER(SAMPConstants.VEHICLE_FARMTRAILER, TextKeys.vehicle.model.name.farmtrailer, "Farm Plow", VehicleType.TRAILER, 0),
    UTILITYTRAILER(SAMPConstants.VEHICLE_UTILITYTRAILER, TextKeys.vehicle.model.name.utilitytrailer, "Utility Trailer", VehicleType.TRAILER, 0);

    companion object : ConstantValueRegistry<Int, VehicleModel>(*VehicleModel.values()) {

        private val vehicleModelsByName = PatriciaTrie<VehicleModel>()

        init {
            // Only index models with unique names
            VehicleModel
                    .values()
                    .groupBy { it.modelName.toLowerCase() }
                    .values
                    .filter { it.size == 1 }
                    .map { it.first() }
                    .forEach {
                        vehicleModelsByName[it.modelName.toLowerCase()] = it
                    }
        }

        operator fun get(modelName: String): VehicleModel? {
            val models = vehicleModelsByName.prefixMap(modelName.toLowerCase()).values
            return when {
                models.size == 1 -> models.first()
                else -> null
            }
        }
    }

}
