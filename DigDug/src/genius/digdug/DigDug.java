package genius.digdug;

import genius.digdug.block.Block;
import genius.digdug.block.BlockColor;
import genius.digdug.block.BlockImage;
import genius.digdug.block.BlockRailGun;
import genius.digdug.entity.Entity;
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
	public static ArrayList<EntityMonster> monstesrs = new ArrayList<EntityMonster>();
	
	public static int PLAYER_SPEED = 5;
	public static int MONSTER_SPEED = 20;
	
	public static Image playerImgRight;
	public static Image playerImgLeft;
	public static Image playerImgDown;
	public static Image playerImgUp;
	
	public static Color POOKA_COLOR = Color.blue;
	public static Color FYGAR_COLOR = Color.lightGray;
	private static int level = 1;
	
	public DigDug() {
		super("DigDug");
	}
	
	@Override
	public synchronized void render(final GameContainer gc, final Graphics g) throws SlickException {
		final Iterator<Entry<Coordinates, ArrayList<Block>>> itr = Map.getMap().entrySet().iterator();
		ArrayList<Block> shaders=new ArrayList<Block>();
		while (itr.hasNext()) {
			shaders.addAll(itr.next().getValue());
		}
		renderAll(shaders, g);
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
	private void renderAll(final ArrayList<Block> list, final Graphics g) {
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
			b.render(g, b.coords.x * 32, b.coords.y * 32);
		});
	}
	
	public static Color[] colorMap() {
		final int colorLevel = Integer.parseInt(String.valueOf(DigDug.level).split("")[0]);
		return colorMap2(colorLevel);
	}
	
	public static Color[] colorMap2(final int colorLevel) {
		if ((colorLevel % 2) != 0) {
			return new Color[] { Color.cyan, Color.green, Color.yellow, Color.orange, Color.red };
		} else {
			return new Color[] { Color.cyan, Color.white, Color.pink, Color.darkGray, Color.magenta };
		}
	}
	
	public static Color colorMap(final int i) {//return new Color[] {Color.cyan, Color.white, Color.pink, Color.black, Color.magenta};
		return colorMap()[i];
	}
	
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
	
	private void generateMap() {
		for (int y = 0; y < 15; y++) {
			this.generateRow(y);
		}
	}
	
	private void generateRow(final int y) {
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
	
	public static ArrayList<Entity> convertBlocksToEntites(final ArrayList<Block> list) {
		final ArrayList<Entity> entites = new ArrayList<Entity>();
		list.stream().forEach(e -> {
			entites.add(e.binded);
		});
		return entites;
	}
	
	@Override
	public synchronized void init(final GameContainer gc) throws SlickException {
		playerImgRight = new Image("res/DigDug.png").getScaledCopy(32, 32);
		playerImgLeft = playerImgRight.getFlippedCopy(true, false);
		playerImgDown = new Image("res/DigDugDown.png");
		playerImgUp = new Image("res/DigDugUp.png");
		this.generateMap();
		player.move(new Coordinates(9, 7));
		final BlockImage playerBlock = new BlockImage(playerImgLeft);
		player.facing = Facing.left;
		Map.add(new Coordinates(9, 7), playerBlock);
		bind(player, playerBlock);
		DigDug.addUpdateTask(new Task() {
			@Override
			public void run(final Object... vars) {
				final Input input = (Input) vars[0];
				final SuperInput sup = new SuperInput(input);
				Facing facing = null;
				if (sup.action()) {
					return;
				} else if (sup.up()) {
					facing = Facing.up;
				} else if (sup.down()) {
					facing = Facing.down;
				} else if (sup.left()) {
					facing = Facing.left;
				} else if (sup.right()) {
					facing = Facing.right;
				}
				if (facing != null) {
					player.isActioning = false;
					player.move(facing);
				}
			}
		}, PLAYER_SPEED, true);
		
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
		
		final EntityMonster pooka = new EntityMonster();
		pooka.move(new Coordinates(6, 6));
		DigDug.bind(pooka, new BlockColor(DigDug.POOKA_COLOR,-90));
		DigDug.addUpdateTask(new Task() {
			@Override
			public void run(final Object... vars) {
				final Iterator<EntityMonster> itr = DigDug.monstesrs.iterator();
				while (itr.hasNext()) {
					final EntityMonster eater = itr.next();
					eater.moveToTarget(player.coords);
				}
			}
		}, DigDug.MONSTER_SPEED, true);
		
		Octopus.filter(BlockColor.class, 90);
		Octopus.filter(BlockRailGun.class, -1);
	}
	
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
	
	public static <T> Object lastIndex(final ArrayList<T> arraylist) {
		if ((arraylist != null) && !arraylist.isEmpty()) {
			return arraylist.get(arraylist.size() - 1);
		} else {
			return null;
		}
	}
	
	public static void bind(final Entity e, final Block b) {
		e.binded = b;
		b.binded = e;
		b.updateCoords();
	}
	
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
	
	public static synchronized void shader(final String id, final Shader shader) {
		shaders.put(id, shader);
	}
	
	public static synchronized void shader(final String id) {
		shaders.remove(id);
	}
	
	public static synchronized void addUpdateTask(final Task t, final int maxPolls) {
		DigDug.addUpdateTask(t, maxPolls, false);
	}
	
	public static synchronized void addUpdateTask(final Task t, final int maxPolls, final boolean noDelete) {
		t.maxPolls = maxPolls;
		t.noDelete = noDelete;
		updateTasks.add(t);
	}
}
