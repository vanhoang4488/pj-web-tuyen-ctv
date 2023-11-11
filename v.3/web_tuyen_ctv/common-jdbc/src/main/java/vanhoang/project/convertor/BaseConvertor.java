package vanhoang.project.convertor;

public interface BaseConvertor<O, I> {

    <O, I> O convertToDTO (I entity);
    <O, I> I convertToEntity (O dto);
}
