# ReifendefromationsSoftware

Java prototype for a **2D tire deformation simulation**.

## Physics model (equations)

We model the tire as `N` nodes on a ring (mass-spring-damper system).

For each node `i`:

- Position: `x_i = (x_i, y_i)`
- Velocity: `v_i`
- Mass: `m`

The equation of motion is:

`m * d²x_i/dt² = F_radial_i + F_tangential_i + F_damping_i + F_contact_i + F_load_i`

### 1) Spring forces

Hooke's law between node `i` and neighbor `j`:

`F_spring(i,j) = k * (|x_j - x_i| - L0) * normalize(x_j - x_i)`

- Tangential springs use neighbors `i-1`, `i+1`
- Radial spring anchors node to wheel center direction:

`F_radial_i = -k_r * (r_i - R0) * e_r`

where `r_i = |x_i - c|`, `e_r = (x_i - c)/r_i`, and `R0` is the unloaded radius.

### 2) Damping

`F_damping_i = -c_d * v_i`

### 3) Ground contact (penalty method)

Ground profile is `y = h(x)`. Penetration depth:

`p_i = h(x_i) - y_i`

If `p_i > 0`, normal contact force:

`F_n = k_g * p_i - c_g * min(v_i_y, 0)`

`F_contact_i = (0, F_n)`

We also apply a simple tangential friction damping during contact:

`F_t = -mu_d * v_i_x`

so in contact: `F_contact_i += (F_t, 0)`.

### 4) Time integration (semi-implicit Euler)

`v_i(t+dt) = v_i(t) + a_i(t) * dt`

`x_i(t+dt) = x_i(t) + v_i(t+dt) * dt`

This is more stable than explicit Euler for stiff springs.

## Scenarios included

1. **Stationary on flat road**
2. **Rolling on flat road**
3. **Rolling over a bump** (Gaussian bump)

## Run

```bash
javac src/*.java
java -cp src Main
```

The program prints deformation metrics and contact patch length estimate.
