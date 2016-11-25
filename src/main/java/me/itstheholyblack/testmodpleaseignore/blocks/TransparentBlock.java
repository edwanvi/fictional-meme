package me.itstheholyblack.testmodpleaseignore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TransparentBlock extends Block {
	public TransparentBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        setUnlocalizedName(unlocalizedName);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(hardness);
        setResistance(resistance);
        setRegistryName(unlocalizedName);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }

    public TransparentBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.ICE, hardness, resistance);
    }

    public TransparentBlock(String unlocalizedName) {
        this(unlocalizedName, 2.0f, 10.0f);
    }
    // grab the model from our unlocalized name
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
    	return layer == BlockRenderLayer.CUTOUT;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
    }
}
