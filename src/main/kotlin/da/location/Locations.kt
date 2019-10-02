package da.location

import io.ktor.locations.Location


@Location("/")
class Index()

@Location("/{id}")
class Data(val id:Int)



