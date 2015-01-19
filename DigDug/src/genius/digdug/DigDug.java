package genius.digdug;

import genius.digdug.block.Block;
import genius.digdug.block.BlockColor;
import genius.digdug.block.BlockImage;
import genius.digdug.block.BlockRailGun;
import genius.digdug.entity.Entity;
import genius.digdug.entity.EntityInfo;
import genius.digdug.entity.EntityMonster;
import genius.digdug.octopus.Octopus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class DigDug extends BasicGame {
	private static volatile HashMap<String, Shader> shaders = new HashMap<String, Shader>();
	private static volatile ArrayList<Task> updateTasks = new ArrayList<Task>();
	
	public static ArrayList<BlockRailGun> railguns = new ArrayList<BlockRailGun>();
	public static ArrayList<Entity> monsters = new ArrayList<Entity>();
	public static ArrayList<EntityInfo> monsterSpawns = new ArrayList<EntityInfo>();
	
	public static int PLAYER_SPEED = 5;
	public static int MONSTER_SPEED = 20;
	
	public static Image playerImgRight;
	public static Image playerImgLeft;
	public static Image playerImgDown;
	public static Image playerImgUp;
	
	public static Image pookaUp;
	public static Image pookaDown;
	public static Image pookaLeft;
	public static Image pookaRight;
	
	public static Image fygarUp;
	public static Image fygarDown;
	public static Image fygarLeft;
	public static Image fygarRight;
	
	private static int level = 1;
	public static boolean frozen = false;
	
	public DigDug() {
		super("DigDug");
	}
	
	/**
	 * render
	 */
	@Override
	public synchronized void render(final GameContainer gc, final Graphics g) throws SlickException {
		final Iterator<Entry<Coordinates, ArrayList<Block>>> itr = Map.getMap().entrySet().iterator();
		final ArrayList<Shader> shaders = new ArrayList<Shader>();
		while (itr.hasNext()) {
			shaders.addAll(itr.next().getValue());
		}
		shaders.addAll(DigDug.shaders.values());
		this.renderAll(shaders, g);
	}
	
	/*private void renderAll(ArrayList<Block> blocks,Graphics g) {
		Iterator<Block> itr = blocks.iterator();
		while (itr.hasNext()) {
			Block b=null;
			b=itr.next();
			if (b !=null) {
				if (b instanceof BlockRailGun) {
					if (!railguns.contains(b)) {
						try {
						//Map.getBlocks(b.coords).remove(b);
						} catch (ConcurrentModificationException cme) {
							System.err.println("concurrent modifcation");
						}
						continue;
					}
				}
				b.render(g, b.coords.x*32, b.coords.y*32);
			}
		}
	}*/
	
	/**
	 * renders an arraylist of blocks
	 * 
	 * @param list an arraylist of blocks
	 * @param g graphics
	 */
	private void renderAll(final ArrayList<Shader> list, final Graphics g) {
		final Comparator<Shader> comparator = (e1, e2) -> {
			if (e1.zindex > e2.zindex) {
				return 1;
			} else {
				return -1;
			}
		};
		list.stream().sorted(comparator).forEach((b) -> {
			if (b instanceof BlockRailGun) {
				if (!railguns.contains(b)) {
					return;
				}
			}
			if ((b instanceof Block) && !frozen) {
				b.render(g, ((Block) b).coords.x * 32, ((Block) b).coords.y * 32);
			} else {
				b.render(g,-1,-1);
			}
		});
	}
	
	/**
	 * generates the colors used in the map
	 * 
	 * @return array of colors
	 */
	public static Color[] colorMap() {
		final int colorLevel = Integer.parseInt(String.valueOf(DigDug.level).split("")[0]);
		return colorMap2(colorLevel);
	}
	
	/**
	 * generates the colors used in the map
	 * 
	 * @param colorLevel the first number in the current level
	 * @return array
	 */
	public static Color[] colorMap2(final int colorLevel) {
		if ((colorLevel % 2) != 0) {
			return new Color[] { Color.cyan, Color.green, Color.yellow, Color.orange, Color.red };
		} else {
			return new Color[] { Color.cyan, Color.white, Color.pink, Color.darkGray, Color.magenta };
		}
	}
	
	/**
	 * generates the color map, and returns the i index
	 * 
	 * @param i the index
	 * @return color
	 */
	public static Color colorMap(final int i) {//return new Color[] {Color.cyan, Color.white, Color.pink, Color.black, Color.magenta};
		return colorMap()[i];
	}
	
	/**
	 * the player entity
	 */
	public static Entity player = new Entity() {
		@Override
		public boolean move(final Facing facing) {
			final BlockImage bi = (BlockImage) this.binded;
			switch (facing) {
				case up:
					bi.img = DigDug.playerImgUp;
					break;
				case down:
					bi.img = DigDug.playerImgDown;
					break;
				case left:
					bi.img = DigDug.playerImgLeft;
					break;
				case right:
					bi.img = DigDug.playerImgRight;
					break;
			}
			return super.move(facing);
		}
		
		@Override
		public boolean move(final Coordinates coords) {
			if (this.canMoveTo(coords)) {
				if (Map.getBlocks(coords).size() > 0) {
					
					if (Map.getBlocks(coords).get(0) instanceof BlockColor) {
						final BlockColor bc = (BlockColor) Map.getBlocks(coords).get(0);
						if (bc.color != colorMap(0)) {
							Map.getBlocks(coords).clear();
						}
					}
				}
			}
			return super.move(coords);
		}
	};
	
	/**
	 * generates the map
	 */
	private static void generateMap() {
		for (int y = 0; y < 15; y++) {
			generateRow(y);
		}
		monsterSpawns.stream().forEach(e -> {
			Class<EntityMonster> container=e.entity();
			Block bind=e.block();
			Coordinates spawn=e.spawn();
			
			String[] delete=new String[]{
					"(5,6)",
					"(5,7)",
					"(5,8)",
					"(0,11)",
					"(1,11)",
					"(2,11)",
					"(3,11)",
					"(17,5)",
					"(17,6)",
					"(17,7)"
			};
			for (String c : delete) {
				Coordinates coords=Coordinates.decode(c);
				Map.delete(coords);
			}
			
			try {
				Entity entity=container.newInstance();
				entity.spawn=spawn;
				entity.coords=entity.spawn;
				bind(entity,bind);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}
	
	/**
	 * generates a row at y
	 * 
	 * @param y the y-axis
	 */
	private static void generateRow(final int y) {
		System.out.println("generating row for level=" + y);
		for (int x = 0; x < 20; x++) {
			if (((y > 2) & (y < 8)) && ((x == 9) || ((y == 7) && ((x == 8) || (x == 10))))) {
				continue;
			}
			//System.out.println("colorLevel="+y/3);
			int colorLevel = y / 3;
			if (colorLevel < 0) {
				colorLevel = 0;
			}
			Map.add(new Coordinates(x, y), new BlockColor(colorMap(colorLevel), -1));
		}
	}
	
	/**
	 * creates an arraylist of the binded entities in the block arraylist
	 * 
	 * @param list arraylist of blocks
	 * @returns an arraylist of the binds of the blocks
	 */
	public static ArrayList<Entity> convertBlocksToEntites(final ArrayList<Block> list) {
		final ArrayList<Entity> entites = new ArrayList<Entity>();
		list.stream().forEach(e -> {
			entites.add(e.binded);
		});
		return entites;
	}
	
	/**
	 * init
	 */
	@Override
	public synchronized void init(final GameContainer gc) throws SlickException {
		playerImgRight = new Image("res/DigDug.png").getScaledCopy(32, 32);
		playerImgLeft = playerImgRight.getFlippedCopy(true, false);
		playerImgDown = new Image("res/DigDugDown.png");
		playerImgUp = new Image("res/DigDugUp.png");
		
		fygarLeft=new Image("res/fygar.png");
		
		pookaLeft=new Image("res/Pooka.png");
		
		player.move(new Coordinates(9, 7));
		final BlockImage playerBlock = new BlockImage(playerImgLeft);
		player.facing = Facing.left;
		Map.add(new Coordinates(9, 7), playerBlock);
		bind(player, playerBlock);
		DigDug.addUpdateTask(new Task() {
			@Override
			public void run(final Object... vars) {
				if (DigDug.monsters.size() == 0) {
					DigDug.handlePlayerWin();
				}
			}
		}, 0, true);
		DigDug.addUpdateTask(new Task() {
			@Override
			public void run(final Object... vars) {
				final Input input = (Input) vars[0];
				final SuperInput sup = new SuperInput(input);
				Facing facing = null;
				if (sup.action() && !frozen) {
					return;
				} else if (sup.action() && frozen) {
					frozen = false;
					shader("dead");
					player.fairize();
					DigDug.monsters.stream().forEach(e -> e.fairize());
					return;
				} else if (sup.up()) {
					facing = Facing.up;
				} else if (sup.down()) {
					facing = Facing.down;
				} else if (sup.left()) {
					facing = Facing.left;
				} else if (sup.right()) {
					facing = Facing.right;
				} else if (sup.mouse()) {
					Coordinates these=new Coordinates(input.getMouseX(),input.getMouseY()).denormalize();
					System.out.println("mouse: "+these);
				}
				if (facing != null) {
					player.isActioning = false;
					player.move(facing);
				}
			}
		}, PLAYER_SPEED, true);
		/**
		 * (5,6)
		 * (5,7)
		 * (5,8)
		 * (0,11)
		 * (1,11)
		 * (2,11)
		 * (3,11)
		 * (17,5)
		 * (17,6)
		 * (17,7)
		 */
		
		DigDug.addUpdateTask(new Task() {
			@Override
			public void run(final Object... vars) {
				final Input input = (Input) vars[0];
				final SuperInput sup = new SuperInput(input);
				/*if (player.isActioning) {
					if (Map.getBlocks(player.coords.facing(player.facing)).size()==0) {
						if (railguns.size()>5) return;
						BlockRailGun lastgun=(BlockRailGun)lastIndex(railguns);
						if (lastgun!=null) {
							Map.add(lastgun.coords.facing(player.facing), new BlockRailGun());
						} else {
							Map.add(player.coords.facing(player.facing), new BlockRailGun());
						}
						player.isActioning=false;
					}
				} */
				if (sup.action()) {
					if (railguns.size() > 5) {
						return;
					}
					/*boolean sret = false;
					if (railguns.size() == 5) {
						final Iterator<BlockRailGun> itr = railguns.iterator();
						while (itr.hasNext()) {
							final BlockRailGun gun = itr.next();
							sret = sret || !(ListWithClass.containsClass(DigDug.convertBlocksToEntites(Map.getBlocks(gun.coords)), Entity.class));
						}
					}
					if (sret) {
						System.out.println("stopping due to stuff; railguns cleared");
						railguns.clear();
						return;
					}*/
					final BlockRailGun lastgun = (BlockRailGun) lastIndex(railguns);
					Coordinates ncoords;
					if (lastgun != null) {
						ncoords = lastgun.coords.facing(player.facing);
					} else {
						ncoords = player.coords.facing(player.facing);
					}
					if (Map.getBlocks(ncoords).size() > 1) {
						return;
					}
					Map.addIfEmpty(ncoords, new BlockRailGun(ncoords));
				} else {
					railguns.clear();
				}
			}
		}, 10, true);
		DigDug.addUpdateTask(new Task() {
			@Override
			public void run(final Object... vars) {
				final Input input = (Input) vars[0];
				final SuperInput sup = new SuperInput(input);
				if (!sup.action()) {
					final Iterator<Entry<Coordinates, ArrayList<Block>>> itr = Map.getMap().entrySet().iterator();
					while (itr.hasNext()) {
						final Entry<Coordinates, ArrayList<Block>> entry = itr.next();
						if ((entry == null) || (entry.getKey() == null) || (entry.getValue() == null)) {
							continue;
						}
						checkAll(entry.getValue());
					}
				}
			}
		}, 0, true);
		DigDug.addUpdateTask(new Task(){
			@Override
			public void run(Object... vars) {
				monsters.stream().forEach(e -> {
					if (e==null) return                    ;
					e.moveToTarget(player.coords);
				});
			}}, DigDug.MONSTER_SPEED, true);
		
		define(EntityMonster.class,new Coordinates(5,6),new BlockImage(DigDug.pookaLeft));
		define(EntityMonster.class,new Coordinates(0,11),new BlockImage(DigDug.fygarLeft));
		define(EntityMonster.class,new Coordinates(17,5),new BlockImage(DigDug.fygarLeft));         
		
		Octopus.filter(Block.class, 5000);
		Octopus.filter(BlockRailGun.class, -1);
		
		DigDug.shader("level", new Shader(){
			@Override
			public void render(Graphics g, float x, float y) {
				g.setColor(Color.white);
				g.drawString("Level: "+level, 10+(80*1), 10);
			}});
		
		DigDug.generateMap();
	}
	
	private void define(Class<EntityMonster> class1,Coordinates spawn,Block b) {
		EntityInfo info=new EntityInfo(class1,b,spawn);
		monsterSpawns.add(info);
	}
	
	protected static void handlePlayerWin() {
		frozen = true;
		level++;
		Map.clear();
		DigDug.generateMap();
		shader("dead", new Shader() {
			@Override
			public void render(final Graphics g, final float x, final float y) {
				g.setColor(Color.white);
				g.drawString("You won! Press SPACE to continue", 9 * 32, 7 * 32);
			}
		});
	}
	
	/**
	 * checks the arraylist for railguns, and removes them if not in the
	 * railguns arraylist
	 * 
	 * @param blocks
	 */
	private static void checkAll(final ArrayList<Block> blocks) {
		final Iterator<Block> itr = blocks.iterator();
		while (itr.hasNext()) {
			final Block b = itr.next();
			if (b instanceof BlockRailGun) {
				if (!railguns.contains(b)) {
					//System.out.println("removing railgun "+b);
					Map.getBlocks(b.coords).remove(b);
				}
			}
		}
	}
	
	/**
	 * returns the last index in an arraylist
	 * 
	 * @param arraylist the arraylist
	 * @return the last index
	 */
	public static <T> Object lastIndex(final ArrayList<T> arraylist) {
		if ((arraylist != null) && !arraylist.isEmpty()) {
			return arraylist.get(arraylist.size() - 1);
		} else {
			return null;
		}
	}
	
	/**
	 * binds an entity to a block<br>
	 * whenever the entity moves, the block updates it position
	 * 
	 * @param e entity
	 * @param b block
	 */
	public static void bind(final Entity e, final Block b) {
		e.binded = b;
		e.spawn = e.coords;
		b.binded = e;
		b.zindex = 10;
		b.updateCoords();
	}
	
	/**
	 * update
	 */
	@Override
	public synchronized void update(final GameContainer gc, final int delta) throws SlickException {
		try {
			final Iterator<Task> tasks = updateTasks.iterator();
			while (tasks.hasNext()) {
				final Task task = tasks.next();
				if (task != null) {
					if (task.poll(gc.getInput())) {
						updateTasks.remove(task);
					}
				}
			}
		} catch (final ConcurrentModificationException cme) {
			//	System.err.println("warning: concurrent modification");
		}
	}
	
	/**
	 * adds a shader
	 * 
	 * @param id a unique id identifying this shader
	 * @param shader the shader
	 */
	public static synchronized void shader(final String id, final Shader shader) {
		shaders.put(id, shader);
	}
	
	/**
	 * removes the identified shader
	 * 
	 * @param id uuid
	 */
	public static synchronized void shader(final String id) {
		shaders.remove(id);
	}
	
	/**
	 * adds a task to be ran at update
	 * 
	 * @param t the task
	 * @param maxPolls the number of times the task will be called before it
	 *            runs
	 */
	public static synchronized void addUpdateTask(final Task t, final int maxPolls) {
		DigDug.addUpdateTask(t, maxPolls, false);
	}
	
	/**
	 * @see DigDug#addUpdateTask(Task, int)
	 * @param t the task
	 * @param maxPolls
	 * @param noDelete if the task should not autodelete after running
	 */
	public static synchronized void addUpdateTask(final Task t, final int maxPolls, final boolean noDelete) {
		t.maxPolls = maxPolls;
		t.noDelete = noDelete;
		updateTasks.add(t);
	}
	
	public static void handlePlayerDeath() {
		frozen = true;
		shader("dead", new Shader() {
			@Override
			public void render(final Graphics g, final float x, final float y) {
				g.setColor(Color.white);
				g.drawString("You died. Press SPACE to continue", 9 * 32, 7 * 32);
			}
		});
	}
}
