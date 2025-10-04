@RestController
@RequestMapping("/api/memories")
@CrossOrigin
public class MemoryController {
    private final MemoryRepository repository;

    public MemoryController(MemoryRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Memory> getAll() { return repository.findAll(); }

    @PostMapping
    public Memory create(@RequestBody Memory memory) { return repository.save(memory); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repository.deleteById(id); }
}
