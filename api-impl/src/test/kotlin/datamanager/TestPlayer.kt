package datamanager
import net.dustrean.api.ICoreAPI
import net.dustrean.api.data.AbstractCacheHandler
import net.dustrean.api.data.AbstractDataObject
import net.dustrean.api.data.ICacheValidator
import java.util.*

class TestPlayer(
    private val uniqueId: UUID = UUID.randomUUID(),
    private val cacheHandler: TestCacheHandler = TestCacheHandler(),
    private val validator: TestValidator = TestValidator()
) : AbstractDataObject() {

    var coins = 0

    override fun update() {
        ICoreAPI.getInstance<TestCoreAPI>().playerManager.updateObject(this)
    }

    override fun getIdentifier(): UUID = uniqueId

    override fun getCacheHandler(): AbstractCacheHandler<AbstractDataObject> = cacheHandler as AbstractCacheHandler<AbstractDataObject>

    override fun getValidator(): ICacheValidator<AbstractDataObject> = validator as ICacheValidator<AbstractDataObject>
}