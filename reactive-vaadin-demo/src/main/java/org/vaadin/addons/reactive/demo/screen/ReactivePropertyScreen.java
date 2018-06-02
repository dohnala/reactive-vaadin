package org.vaadin.addons.reactive.demo.screen;

import javax.annotation.Nonnull;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import io.reactivex.Observable;
import org.vaadin.addons.reactive.ReactiveProperty;
import org.vaadin.addons.reactive.mvvm.ReactiveView;
import org.vaadin.addons.reactive.mvvm.ReactiveViewModel;

/**
 * @author dohnal
 */
@SpringView(name = ReactivePropertyScreen.SCREEN_NAME)
public class ReactivePropertyScreen extends AbstractDemoSectionScreen
{
    public static final String SCREEN_NAME = "property";

    public static final String SECTION_1 = "section1";
    public static final String SECTION_2 = "section2";
    public static final String SECTION_3 = "section3";
    public static final String SECTION_4 = "section4";
    public static final String SECTION_5 = "section5";
    public static final String SECTION_6 = "section6";
    public static final String SECTION_7 = "section7";

    public ReactivePropertyScreen()
    {
        super("demo/reactive_property.yaml");
    }

    @Override
    protected Component createSection(final @Nonnull String section)
    {
        switch (section)
        {
            case SECTION_1:
                return createSection1();
            case SECTION_2:
                return createSection2();
            case SECTION_3:
                return createSection3();
            case SECTION_4:
                return createSection4();
            case SECTION_5:
                return createSection5();
            case SECTION_6:
                return createSection6();
            case SECTION_7:
                return createSection7();
        }

        return null;
    }

    @Nonnull
    private ReactiveView createSection1()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<String> property;

            public DemoViewModel()
            {
                this.property = createProperty();
            }

            @Nonnull
            public ReactiveProperty<String> getProperty()
            {
                return property;
            }

            @Nonnull
            public Observable<String> getObservable()
            {
                return property.asObservable().map(String::toLowerCase);
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final TextField textField = new TextField();
                textField.setPlaceholder("Type...");

                final Label label = new Label();

                bind(valueOf(textField)).to(viewModel.getProperty());
                bind(valueOf(label)).to(viewModel.getObservable());

                setCompositionRoot(new VerticalLayout(textField, label));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection2()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<String> property;

            public DemoViewModel()
            {
                this.property = createProperty("Default value");
            }

            @Nonnull
            public ReactiveProperty<String> getProperty()
            {
                return property;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final TextField textField = new TextField();

                bind(valueOf(textField)).to(viewModel.getProperty());

                setCompositionRoot(new VerticalLayout(textField));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection3()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<Double> property;

            public DemoViewModel()
            {
                this.property = createProperty(50.0);
            }

            @Nonnull
            public ReactiveProperty<Double> getProperty()
            {
                return property;
            }

            public void reset()
            {
                property.setValue(50.0);
            }

            public void increment()
            {
                property.updateValue(value -> Math.min(value + 5, 100.0));
            }

            public void decrement()
            {
                property.updateValue(value -> Math.max(value - 5, 0.0));
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Slider slider = new Slider();
                slider.setMin(0.0);
                slider.setMax(100.0);
                slider.setWidth(200, Unit.PIXELS);

                final Button decrement = new Button(VaadinIcons.MINUS, event ->
                        viewModel.decrement());

                final Button reset = new Button("Reset", event ->
                        viewModel.reset());

                final Button increment = new Button(VaadinIcons.PLUS, event ->
                        viewModel.increment());

                bind(valueOf(slider)).to(viewModel.getProperty());

                setCompositionRoot(new VerticalLayout(slider,
                        new HorizontalLayout(decrement, reset, increment)));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection4()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<String> propertyA;
            private final ReactiveProperty<String> propertyB;
            private final ReactiveProperty<String> result;

            public DemoViewModel()
            {
                this.propertyA = createProperty();
                this.propertyB = createProperty();
                this.result = createPropertyFrom(propertyA, propertyB);
            }

            @Nonnull
            public ReactiveProperty<String> getPropertyA()
            {
                return propertyA;
            }

            @Nonnull
            public ReactiveProperty<String> getPropertyB()
            {
                return propertyB;
            }

            @Nonnull
            public ReactiveProperty<String> getResult()
            {
                return result;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final TextField textFieldA = new TextField();
                textFieldA.setPlaceholder("Type...");

                final TextField textFieldB = new TextField();
                textFieldB.setPlaceholder("Type...");

                final Label result = new Label();

                bind(valueOf(textFieldA)).to(viewModel.getPropertyA());
                bind(valueOf(textFieldB)).to(viewModel.getPropertyB());
                bind(valueOf(result)).to(viewModel.getResult());

                setCompositionRoot(new VerticalLayout(textFieldA, textFieldB, result));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection5()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<String> propertyA;
            private final ReactiveProperty<String> propertyB;
            private final ReactiveProperty<String> result;

            public DemoViewModel()
            {
                this.propertyA = createProperty();
                this.propertyB = createProperty();
                this.result = createPropertyFrom(propertyA, propertyB, (x, y) ->
                        x.toLowerCase() + "." + y.toLowerCase());
            }

            @Nonnull
            public ReactiveProperty<String> getPropertyA()
            {
                return propertyA;
            }

            @Nonnull
            public ReactiveProperty<String> getPropertyB()
            {
                return propertyB;
            }

            @Nonnull
            public ReactiveProperty<String> getResult()
            {
                return result;
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final TextField textFieldA = new TextField();
                textFieldA.setPlaceholder("Type...");

                final TextField textFieldB = new TextField();
                textFieldB.setPlaceholder("Type...");

                final Label result = new Label();

                bind(valueOf(textFieldA)).to(viewModel.getPropertyA());
                bind(valueOf(textFieldB)).to(viewModel.getPropertyB());
                bind(valueOf(result)).to(viewModel.getResult());

                setCompositionRoot(new VerticalLayout(textFieldA, textFieldB, result));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection6()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<Integer> property;

            public DemoViewModel()
            {
                this.property = createProperty(0);
            }

            @Nonnull
            public Observable<String> getObservable()
            {
                return property.asObservable().map(Object::toString);
            }

            public void update()
            {
                property.suppress(() -> {
                    property.updateValue(value -> value + 1);
                    property.updateValue(value -> value + 1);
                    property.updateValue(value -> value + 1);
                });
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Label label = new Label();
                final Button update = new Button("Update", event ->
                        viewModel.update());

                bind(valueOf(label)).to(viewModel.getObservable());

                setCompositionRoot(new VerticalLayout(label, update));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }

    @Nonnull
    private ReactiveView createSection7()
    {
        class DemoViewModel extends ReactiveViewModel
        {
            private final ReactiveProperty<Integer> property;

            public DemoViewModel()
            {
                this.property = createProperty(0);
            }

            @Nonnull
            public Observable<String> getObservable()
            {
                return property.asObservable().map(Object::toString);
            }

            public void update()
            {
                property.delay(() -> {
                    property.updateValue(value -> value + 1);
                    property.updateValue(value -> value + 1);
                    property.updateValue(value -> value + 1);
                });
            }
        }

        class DemoView extends ReactiveView<DemoViewModel>
        {
            @Override
            protected void initView(final @Nonnull DemoViewModel viewModel)
            {
                final Label label = new Label();
                final Button update = new Button("Update", event ->
                        viewModel.update());

                bind(valueOf(label)).to(viewModel.getObservable());

                setCompositionRoot(new VerticalLayout(label, update));
            }
        }

        return new DemoView().withViewModel(new DemoViewModel());
    }
}
