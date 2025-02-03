import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import app.cash.turbine.test
import com.maggiver.movieshd.core.valueObject.ResourceState
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieCustom
import com.maggiver.movieshd.homeMovie.data.provider.remote.model.MovieSearchCustom
import com.maggiver.movieshd.homeMovie.domain.MovieUseCaseContract
import com.maggiver.movieshd.homeMovie.presentation.NowPlayingViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.manipulation.Ordering.Context
import org.mockito.junit.jupiter.MockitoExtension

@OptIn(ExperimentalCoroutinesApi::class)
class NowPlayingViewModelTest {

    // Reglas para ejecutar LiveData en pruebas
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Dispatcher para pruebas de coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private val useCase: MovieUseCaseContract = mockk()
    private val context: Context <= mockk(relaxed = true) // relaxed evita inicializar todo el objeto
    private val savedStateHandle = SavedStateHandle(mapOf("searchMovie" to "comedia"))

    // ViewModel bajo prueba
    private lateinit var viewModel: NowPlayingViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NowPlayingViewModel(useCase, context, savedStateHandle)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `nowPlayingMovies should update uiState with movie list`() = runTest {
        // Datos de prueba
        val movies = listOf(MovieCustom(id = 1, title = "Test Movie"))
        val expectedState = ResourceState.SuccessState(movies)

        // Mock del useCase para devolver una respuesta exitosa
        coEvery { useCase.invoke(any()) } returns flowOf(expectedState)

        // Llamamos a la función del ViewModel
        viewModel.nowPlayingMovies(context)

        // Verificamos que el estado cambió correctamente
        viewModel.uiState.test {
            assert(awaitItem() is ResourceState.LoadingState)
            assert(awaitItem() == expectedState)
        }
    }

    @Test
    fun `fetchMovieSearch should update movieSearchResults with search results`() = runTest {
        // Datos de prueba
        val searchResults = listOf(MovieSearchCustom(id = 2, title = "Search Movie"))
        val expectedState = ResourceState.SuccessState(searchResults)

        // Mock del useCase para devolver resultados de búsqueda
        coEvery { useCase.getSearchMovieUseCase(context, "comedia") } returns expectedState

        // Simular LiveData observando los cambios
        val liveData = viewModel.fetchMovieSearch().asLiveData()

        // Disparamos la primera emisión de datos
        advanceUntilIdle()

        // Verificamos el resultado
        assert(liveData.value is ResourceState.LoadingState)
        assert(liveData.value == expectedState)
    }
}
