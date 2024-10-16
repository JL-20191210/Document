`UNION` 和 `UNION ALL` 都是用于合并两个或多个 `SELECT` 语句的结果集，但它们之间有以下区别：

### 1. **去重**
- **`UNION`**：会对结果集进行去重，合并后的结果集中不会包含重复的行。这意味着它在合并结果集之后，会执行一个额外的排序操作来移除重复数据。
- **`UNION ALL`**：不会去重，直接合并所有结果集，包括重复的行。因此，它比 `UNION` 快，因为它不需要做去重操作。

### 2. **性能**
- **`UNION`**：由于有去重操作，性能相对较低。数据库在合并结果集之后还需要排序并删除重复的记录，尤其在结果集较大时，性能开销明显。
- **`UNION ALL`**：没有去重操作，性能更好，尤其在合并大数据集时，`UNION ALL` 的速度明显快于 `UNION`。

### 3. **使用场景**
- **`UNION`**：适用于你明确知道合并的结果集中可能存在重复值，并且希望去除这些重复行的场景。
- **`UNION ALL`**：适用于你确定数据集没有重复行，或者即使有重复行也不需要去重的场景。通常在追求更高的性能时，使用 `UNION ALL` 更为合适。

### 示例

假设有两个表 `table1` 和 `table2`，分别包含以下数据：

**`table1`**：
| id   | name  |
| ---- | ----- |
| 1    | Alice |
| 2    | Bob   |

**`table2`**：
| id   | name  |
| ---- | ----- |
| 2    | Bob   |
| 3    | Carol |

#### 使用 `UNION`：

```sql
SELECT id, name FROM table1
UNION
SELECT id, name FROM table2;
```

结果：
| id   | name  |
| ---- | ----- |
| 1    | Alice |
| 2    | Bob   |
| 3    | Carol |

（**去重**，只保留了一行 `id = 2, name = Bob`）

#### 使用 `UNION ALL`：

```sql
SELECT id, name FROM table1
UNION ALL
SELECT id, name FROM table2;
```

结果：
| id   | name  |
| ---- | ----- |
| 1    | Alice |
| 2    | Bob   |
| 2    | Bob   |
| 3    | Carol |

（**不去重**，两个 `Bob` 都保留）

### 总结
- **`UNION`** 去重，性能稍慢，适合需要唯一结果的情况。
- **`UNION ALL`** 不去重，性能较快，适合不需要唯一性的情况。