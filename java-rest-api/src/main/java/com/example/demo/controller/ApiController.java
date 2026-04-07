import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/items")
public class ApiController {

    private List<Item> items = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(items);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createItem(@RequestBody Item item) {

        if (item.getName() == null || item.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório");
        }

        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            return ResponseEntity.badRequest().body("Descrição é obrigatória");
        }

        item.setId(counter.incrementAndGet());
        items.add(item);

        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return items.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint 1: Buscar por nome
    @GetMapping("/search")
    public ResponseEntity<?> getByName(@RequestParam String name) {

        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório");
        }

        return items.stream()
                .filter(item -> name.equalsIgnoreCase(item.getName()))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado"));
    }

    // Endpoint 2: Atualizar descrição
    @PatchMapping("/{id}/description")
    public ResponseEntity<?> updateDescription(@PathVariable Long id, @RequestBody String description) {

        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Descrição não pode ser vazia");
        }

        for (Item item : items) {
            if (id.equals(item.getId())) {
                item.setDescription(description);
                return ResponseEntity.ok(item);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado");
    }
}
