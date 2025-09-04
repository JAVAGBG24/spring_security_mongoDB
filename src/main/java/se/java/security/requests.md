# Product Filter Test Requests

## 1. Basic Single Filter Tests

### Common Filters (Available for All Product Types)

**Test Name Filtering:**
```
GET /test?productType=collar&name=Premium
GET /test?productType=collar&name=leather
GET /test?productType=leash&name=Retractable
GET /test?productType=toy&name=Squeaky
GET /test?productType=bowl&name=Steel
```

**Test Color Filtering:**
```
GET /test?productType=collar&color=brown
GET /test?productType=collar&color=red
GET /test?productType=leash&color=black
GET /test?productType=toy&color=yellow
GET /test?productType=bowl&color=silver
```

**Test Price Range Filtering:**
```
GET /test?productType=collar&minPrice=20.00
GET /test?productType=collar&maxPrice=30.00
GET /test?productType=leash&minPrice=25.00&maxPrice=40.00
GET /test?productType=toy&maxPrice=10.00
GET /test?productType=bowl&minPrice=15.00
```

## 2. Product-Specific Filter Tests

### Collar-Specific Filters
```
GET /test?productType=collar&size=M
GET /test?productType=collar&size=S
GET /test?productType=collar&size=L
GET /test?productType=collar&material=leather
GET /test?productType=collar&material=nylon
GET /test?productType=collar&material=fabric
```

### Leash-Specific Filters
```
GET /test?productType=leash&length=6.0
GET /test?productType=leash&length=4.0
GET /test?productType=leash&length=5.0
GET /test?productType=leash&material=nylon
GET /test?productType=leash&material=chain
GET /test?productType=leash&material=leather
```

### Toy-Specific Filters
```
GET /test?productType=toy&type=ball
GET /test?productType=toy&type=rope
GET /test?productType=toy&type=puzzle
```

### Bowl-Specific Filters
```
GET /test?productType=bowl&capacity=2.0
GET /test?productType=bowl&capacity=1.5
GET /test?productType=bowl&capacity=3.0
```

## 3. Complex Multi-Filter Combinations

### Collar Complex Filtering
```
GET /test?productType=collar&color=red&size=M&material=leather
GET /test?productType=collar&name=Premium&color=brown&material=leather
GET /test?productType=collar&size=S&color=blue&minPrice=10.00&maxPrice=15.00
GET /test?productType=collar&material=nylon&color=red&maxPrice=20.00
GET /test?productType=collar&size=M&minPrice=25.00&maxPrice=50.00
```

### Leash Complex Filtering
```
GET /test?productType=leash&color=brown&length=5.0&material=leather
GET /test?productType=leash&material=chain&length=4.0&minPrice=30.00
GET /test?productType=leash&color=black&material=nylon&maxPrice=25.00
GET /test?productType=leash&length=6.0&minPrice=20.00&maxPrice=30.00
```

### Toy Complex Filtering
```
GET /test?productType=toy&type=ball&color=yellow&maxPrice=10.00
GET /test?productType=toy&type=puzzle&color=blue&minPrice=20.00
GET /test?productType=toy&name=Rope&type=rope&color=white
GET /test?productType=toy&type=ball&minPrice=5.00&maxPrice=15.00
```

### Bowl Complex Filtering
```
GET /test?productType=bowl&capacity=2.0&color=silver&minPrice=15.00
GET /test?productType=bowl&name=Ceramic&capacity=1.5&color=blue
GET /test?productType=bowl&capacity=3.0&maxPrice=15.00
GET /test?productType=bowl&color=black&minPrice=10.00&maxPrice=20.00
```

## 4. Edge Case and Validation Tests

### Invalid Product Type
```
GET /test?productType=invalidType&name=test
```

### Missing Product Type (Should Default to Base Product)
```
GET /test?name=Premium&color=brown
GET /test?color=red&minPrice=10.00
```

### Invalid Filter Parameters
```
GET /test?productType=collar&invalidParam=test&size=M
GET /test?productType=leash&length=invalidNumber
GET /test?productType=bowl&capacity=notANumber
GET /test?productType=toy&unknownFilter=value
```

### Case Sensitivity Tests
```
GET /test?productType=COLLAR&size=m&material=LEATHER
GET /test?productType=toy&type=BALL&color=YELLOW
GET /test?productType=leash&material=NYLON&color=BLACK
```

## 5. Performance and Boundary Tests

### Maximum Parameter Combinations
```
GET /test?productType=collar&name=Premium&color=brown&size=M&material=leather&minPrice=25.00&maxPrice=35.00
```

### Empty String Parameters
```
GET /test?productType=collar&name=&size=M
GET /test?productType=leash&material=&length=5.0
```

### Extreme Price Values
```
GET /test?productType=collar&minPrice=0.01
GET /test?productType=leash&maxPrice=9999.99
GET /test?productType=toy&minPrice=100.00&maxPrice=50.00
```

### No Results Expected
```
GET /test?productType=collar&size=XXL
GET /test?productType=leash&length=10.0
GET /test?productType=toy&type=frisbee
GET /test?productType=bowl&capacity=10.0
```

## 6. Learning Exercise Requests

### Understanding Filter Precedence
```
# Test to see if both tier 1 (DB) and tier 2 (predicate) filters work
GET /test?productType=collar&name=Premium&size=M

# Compare results with broader search
GET /test?productType=collar&name=Premium

# Single vs multiple filters
GET /test?productType=collar&material=leather
GET /test?productType=collar&material=leather&color=brown
```

### Testing Filter Combinations
```
# Single filter - should return 4 collars
GET /test?productType=collar

# Add color filter - should return 2 red collars
GET /test?productType=collar&color=red

# Add material filter - should return 1 red leather collar
GET /test?productType=collar&color=red&material=leather

# Add price range - should return 1 collar (Designer Red Collar)
GET /test?productType=collar&color=red&material=leather&minPrice=40.00
```

### Cross-Product Type Comparisons
```
# Same color across different product types
GET /test?productType=collar&color=red
GET /test?productType=leash&color=black
GET /test?productType=toy&color=blue
GET /test?productType=bowl&color=silver

# Price comparison across types
GET /test?productType=collar&minPrice=20.00
GET /test?productType=leash&minPrice=20.00
GET /test?productType=toy&minPrice=20.00
GET /test?productType=bowl&minPrice=20.00
```

### Material Filter Tests
```
# Leather products
GET /test?productType=collar&material=leather
GET /test?productType=leash&material=leather

# Nylon products
GET /test?productType=collar&material=nylon
GET /test?productType=leash&material=nylon
```

